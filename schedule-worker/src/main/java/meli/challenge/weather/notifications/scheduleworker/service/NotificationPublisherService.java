package meli.challenge.weather.notifications.scheduleworker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meli.challenge.weather.notifications.scheduleworker.config.RabbitConfig;
import meli.challenge.weather.notifications.scheduleworker.deadletter.ProducerDeadLetterService;
import meli.challenge.weather.notifications.scheduleworker.model.dto.NotificationMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationPublisherService {

    private final RabbitTemplate rabbitTemplate;

    public void publishNotification(NotificationMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NOTIFICATIONS,
                RabbitConfig.ROUTING_KEY_ENVIOS,
                message
        );
    }
}
