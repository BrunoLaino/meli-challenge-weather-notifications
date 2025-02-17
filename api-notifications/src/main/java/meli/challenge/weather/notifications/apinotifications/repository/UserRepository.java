package meli.challenge.weather.notifications.apinotifications.repository;


import meli.challenge.weather.notifications.apinotifications.model.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Long> {
}