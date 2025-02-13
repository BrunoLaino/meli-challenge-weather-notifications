package meli.challenge.weather.notifications.apinotifications.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "scheduled_notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String cityName;
    private String cityId;
    private boolean litoranea;
    private LocalDateTime scheduledTime;
    private boolean sent;

}