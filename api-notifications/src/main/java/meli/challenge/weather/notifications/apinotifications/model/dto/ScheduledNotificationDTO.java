package meli.challenge.weather.notifications.apinotifications.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import meli.challenge.weather.notifications.apinotifications.model.domain.ScheduledNotification;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledNotificationDTO {
    private Long id;
    private Long userId;
    private String cityId;
    private LocalDateTime scheduledTime;
    private boolean sent;

    public ScheduledNotificationDTO(ScheduledNotification sn) {
        this.id = sn.getId();
        this.userId = sn.getUserId();
        this.cityId = sn.getCityName();
        this.scheduledTime = sn.getScheduledTime();
        this.sent = sn.isSent();
    }
}