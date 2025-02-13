package meli.challenge.weather.notifications.apinotifications.controller;


import lombok.RequiredArgsConstructor;
import meli.challenge.weather.notifications.apinotifications.model.domain.User;
import meli.challenge.weather.notifications.apinotifications.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/{id}/opt-out")
    public ResponseEntity<Void> optOut(@PathVariable Long id) {
        userService.optOutUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/opt-in")
    public ResponseEntity<Void> optIn(@PathVariable Long id) {
        userService.optInUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/")
    public ResponseEntity<User> createOrUpdate(@RequestBody User user) {
        User respoonseUser = userService.createOrUpdate(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(respoonseUser);
    }

}