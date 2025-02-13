package meli.challenge.weather.notifications.notificationsender.repository;


import meli.challenge.weather.notifications.notificationsender.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}