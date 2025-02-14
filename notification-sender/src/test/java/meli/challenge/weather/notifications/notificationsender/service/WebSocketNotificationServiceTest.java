package meli.challenge.weather.notifications.notificationsender.service;

import meli.challenge.weather.notifications.notificationsender.service.WebSocketNotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WebSocketNotificationServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private WebSocketNotificationService webSocketNotificationService;

    @Test
    void testSendNotification() {
        String notification = "Test notification message";
        webSocketNotificationService.sendNotification(notification);
        verify(messagingTemplate).convertAndSend("/topic/notifications", notification);
    }
}
