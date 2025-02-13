package meli.challenge.weather.notifications.notificationsender.deadletter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meli.challenge.weather.notifications.notificationsender.config.RabbitConfig;
import meli.challenge.weather.notifications.notificationsender.model.dto.DeadLetterRecordDTO;
import meli.challenge.weather.notifications.notificationsender.model.dto.NotificationMessageDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeadLetterService {

    private final RabbitTemplate rabbitTemplate;

    public void sendToDeadLetter(NotificationMessageDTO message, Exception e) {
        log.error("Encaminhando mensagem para a Dead Letter Queue. Motivo: {}. Mensagem: {}", e.getMessage(), message);

        DeadLetterRecordDTO dto = new DeadLetterRecordDTO(
                message.toString(),
                e.getMessage(),
                LocalDateTime.now()
        );

        rabbitTemplate.convertAndSend(
                RabbitConfig.DEAD_LETTER_EXCHANGE,
                RabbitConfig.DEAD_LETTER_ROUTING_KEY,
                dto
        );
    }
}
