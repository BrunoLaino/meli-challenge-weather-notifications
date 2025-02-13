package meli.challenge.weather.notifications.notificationsender.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    public void sendNotification(String notification) {
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
}
