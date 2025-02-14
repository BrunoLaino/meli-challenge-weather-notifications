package meli.challenge.weather.notifications.scheduleworker.repository;

import meli.challenge.weather.notifications.scheduleworker.model.domain.ScheduledNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledNotificationRepository extends JpaRepository<ScheduledNotification, Long> {

    @Transactional(readOnly = true)
    List<ScheduledNotification> findByScheduledTimeLessThanEqualAndSentFalse(LocalDateTime now);
}
