package meli.challenge.weather.notifications.apinotifications.repository;


import meli.challenge.weather.notifications.apinotifications.model.domain.ScheduledNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledNotificationRepository extends JpaRepository<ScheduledNotification, Long> {
    List<ScheduledNotification> findBySentFalseAndScheduledTimeBefore(LocalDateTime dateTime);
}