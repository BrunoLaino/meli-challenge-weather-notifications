package meli.challenge.weather.notifications.apinotifications.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "scheduled_notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledNotification {

    @Id
    private String id;

    private Long userId;
    private String cityName;
    private String cityId;
    private boolean litoranea;
    private LocalDateTime scheduledTime;
    private boolean sent;
    private boolean failed;
}