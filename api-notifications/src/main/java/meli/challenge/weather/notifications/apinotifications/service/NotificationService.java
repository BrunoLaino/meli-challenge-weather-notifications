package meli.challenge.weather.notifications.apinotifications.service;


import lombok.RequiredArgsConstructor;
import meli.challenge.weather.notifications.apinotifications.model.domain.ScheduledNotification;
import meli.challenge.weather.notifications.apinotifications.model.domain.User;
import meli.challenge.weather.notifications.apinotifications.model.dto.ScheduleRequestDTO;
import meli.challenge.weather.notifications.apinotifications.repository.ScheduledNotificationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final ScheduledNotificationRepository scheduledNotificationRepository;
    private final UserService userService;

    public void schedule(ScheduleRequestDTO request) {
        User user = userService.getUser(request);

        ScheduledNotification sn = getScheduledNotification(request, user);

        saveScheduledNotification(sn);
    }

    private void saveScheduledNotification(ScheduledNotification sn) {
        scheduledNotificationRepository.save(sn);
    }

    private static ScheduledNotification getScheduledNotification(ScheduleRequestDTO request, User user) {
        ScheduledNotification sn = new ScheduledNotification();
        sn.setUserId(user.getId());
        sn.setLitoranea(request.isLitoranea());
        sn.setCityName(request.getCityName());
        sn.setScheduledTime(request.getWhen());
        sn.setSent(false);
        return sn;
    }


}
