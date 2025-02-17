package meli.challenge.weather.notifications.scheduleworker.repository;

import meli.challenge.weather.notifications.scheduleworker.model.domain.ScheduledNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledNotificationRepository extends MongoRepository<ScheduledNotification, String> {

    @Transactional(readOnly = true)
    List<ScheduledNotification> findByScheduledTimeLessThanEqualAndSentFalseAndFailedFalse(LocalDateTime now);
}
