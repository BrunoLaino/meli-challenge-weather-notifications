package meli.challenge.weather.notifications.notificationsender.service.channel;


import meli.challenge.weather.notifications.notificationsender.model.dto.NotificationMessageDTO;

public interface NotificationChannel {
    boolean supports(NotificationMessageDTO message);
    void send(NotificationMessageDTO message);
}