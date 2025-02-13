package meli.challenge.weather.notifications.notificationsender.service.channel;

import meli.challenge.weather.notifications.notificationsender.model.dto.NotificationMessageDTO;
import org.springframework.stereotype.Component;

@Component
public class PushNotificationChannel implements NotificationChannel {

    @Override
    public boolean supports(NotificationMessageDTO message) {
        return false;
    }

    @Override
    public void send(NotificationMessageDTO message) {
        System.out.println("[PUSH] Enviando push (futuro)...");
    }
}