package meli.challenge.weather.notifications.notificationsender.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meli.challenge.weather.notifications.notificationsender.config.RabbitConfig;
import meli.challenge.weather.notifications.notificationsender.model.dto.NotificationMessageDTO;
import meli.challenge.weather.notifications.notificationsender.processor.NotificationMessageProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendNotificationConsumer {

    private final NotificationMessageProcessor messageProcessor;

    @RabbitListener(queues = RabbitConfig.QUEUE_ENVIOS)
    public void consume(NotificationMessageDTO message) {
        log.info("Mensagem recebida: {}", message);
        try {
            messageProcessor.process(message);
        } catch (Exception e) {
            log.error("Erro inesperado no processamento da mensagem: {}. Mensagem poderá ser reprocessada.", message, e);
            throw e;
        }
    }
}