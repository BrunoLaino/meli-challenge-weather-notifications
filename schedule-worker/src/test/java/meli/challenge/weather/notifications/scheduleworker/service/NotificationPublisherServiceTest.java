package meli.challenge.weather.notifications.scheduleworker.service;

import meli.challenge.weather.notifications.scheduleworker.config.RabbitConfig;
import meli.challenge.weather.notifications.scheduleworker.model.dto.NotificationMessage;
import meli.challenge.weather.notifications.scheduleworker.service.NotificationPublisherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationPublisherServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private NotificationPublisherService notificationPublisherService;

    @Test
    void testPublishNotification() {
        NotificationMessage message = new NotificationMessage();
        notificationPublisherService.publishNotification(message);
        verify(rabbitTemplate).convertAndSend(
                RabbitConfig.EXCHANGE_NOTIFICATIONS,
                RabbitConfig.ROUTING_KEY_ENVIOS,
                message
        );
    }
}
