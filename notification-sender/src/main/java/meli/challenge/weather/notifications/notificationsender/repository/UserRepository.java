package meli.challenge.weather.notifications.notificationsender.repository;


import meli.challenge.weather.notifications.notificationsender.model.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {
}