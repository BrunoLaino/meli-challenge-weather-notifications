package meli.challenge.weather.notifications.apinotifications.service;

import lombok.RequiredArgsConstructor;
import meli.challenge.weather.notifications.apinotifications.model.domain.User;
import meli.challenge.weather.notifications.apinotifications.model.dto.ScheduleRequestDTO;
import meli.challenge.weather.notifications.apinotifications.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(ScheduleRequestDTO request) {
        return userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuário não encontrado: " + request.getUserId())
                );
    }

    public User optOutUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        user.setOptOut(true);
        return userRepository.save(user);
    }

    public User optInUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        user.setOptOut(false);
        return userRepository.save(user);
    }

    public User createOrUpdate(User user) {
        return userRepository.save(user);
    }
}