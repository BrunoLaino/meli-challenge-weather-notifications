package meli.challenge.weather.notifications.apinotifications.controller;


import lombok.RequiredArgsConstructor;
import meli.challenge.weather.notifications.apinotifications.model.dto.ScheduleRequestDTO;
import meli.challenge.weather.notifications.apinotifications.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/schedule")
    public ResponseEntity<Void> scheduleNotification(@RequestBody ScheduleRequestDTO request) {
        notificationService.schedule(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}