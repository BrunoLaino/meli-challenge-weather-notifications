package meli.challenge.weather.notifications.notificationsender.repository;

import meli.challenge.weather.notifications.notificationsender.model.domain.DeadLetterRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeadLetterRecordRepository extends MongoRepository<DeadLetterRecord, Long> {
}