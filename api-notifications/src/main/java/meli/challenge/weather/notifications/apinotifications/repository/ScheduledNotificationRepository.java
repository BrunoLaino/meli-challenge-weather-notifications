package meli.challenge.weather.notifications.apinotifications.repository;


import meli.challenge.weather.notifications.apinotifications.model.domain.ScheduledNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduledNotificationRepository extends MongoRepository<ScheduledNotification, String> {

}