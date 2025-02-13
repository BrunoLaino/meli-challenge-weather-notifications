package meli.challenge.weather.notifications.notificationsender.service;


import lombok.RequiredArgsConstructor;
import meli.challenge.weather.notifications.notificationsender.model.dto.NotificationMessageDTO;
import meli.challenge.weather.notifications.notificationsender.service.channel.NotificationChannel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationDeliveryService {

    private final List<NotificationChannel> channels;

    public void deliver(NotificationMessageDTO message) {
        for (NotificationChannel channel : channels) {
            if (channel.supports(message)) {
                channel.send(message);
                return;
            }
        }
        System.out.println("Nenhum canal suportou a mensagem: " + message);
    }
}
