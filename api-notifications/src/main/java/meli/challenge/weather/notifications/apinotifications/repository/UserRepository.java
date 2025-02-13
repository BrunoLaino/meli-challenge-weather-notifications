package meli.challenge.weather.notifications.apinotifications.repository;


import meli.challenge.weather.notifications.apinotifications.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}