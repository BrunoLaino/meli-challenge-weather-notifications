package meli.challenge.weather.notifications.notificationsender.processor;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meli.challenge.weather.notifications.notificationsender.deadletter.DeadLetterService;
import meli.challenge.weather.notifications.notificationsender.model.domain.User;
import meli.challenge.weather.notifications.notificationsender.model.dto.NotificationMessageDTO;
import meli.challenge.weather.notifications.notificationsender.repository.UserRepository;
import meli.challenge.weather.notifications.notificationsender.service.NotificationDeliveryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationMessageProcessor {

    private final UserRepository userRepo;
    private final NotificationDeliveryService deliveryService;
    private final DeadLetterService deadLetterService;

    public void process(NotificationMessageDTO message) {
        log.info("Processando mensagem de notificação: {}", message);

        try {
            User user = getUser(message);

            isOptOut(user);

            deliveryService.deliver(message);
            log.info("Notificação entregue com sucesso para o usuário {}", user.getId());
        } catch (ResponseStatusException e) {
            log.error("Erro não recuperável: {}. Encaminhando mensagem para a Dead Letter Queue.", e.getMessage());
            deadLetterService.sendToDeadLetter(message, e);
        }
    }

    private User getUser(NotificationMessageDTO message) {
        return userRepo.findById(message.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuário " + message.getUserId() + " não encontrado"));
    }

    private void isOptOut(User user) {
        if (user.isOptOut()) {
            log.warn("Usuário ID: {} está em opt-out. Notificação descartada.", user.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Usuário ID" + user.getId() + " está em opt-out.");
        }
    }
}