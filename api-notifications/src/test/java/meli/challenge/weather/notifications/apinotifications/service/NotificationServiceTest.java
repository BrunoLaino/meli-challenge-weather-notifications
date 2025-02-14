package meli.challenge.weather.notifications.apinotifications.service;

import meli.challenge.weather.notifications.apinotifications.model.domain.ScheduledNotification;
import meli.challenge.weather.notifications.apinotifications.model.domain.User;
import meli.challenge.weather.notifications.apinotifications.model.dto.ScheduleRequestDTO;
import meli.challenge.weather.notifications.apinotifications.repository.ScheduledNotificationRepository;
import meli.challenge.weather.notifications.apinotifications.service.NotificationService;
import meli.challenge.weather.notifications.apinotifications.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private ScheduledNotificationRepository scheduledNotificationRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void testSchedule() {
        ScheduleRequestDTO request = new ScheduleRequestDTO();
        request.setLitoranea(true);
        request.setCityName("Test City");
        LocalDateTime when = LocalDateTime.now().plusDays(1);
        request.setWhen(when);

        User user = new User();
        user.setId(10L);
        when(userService.getUser(request)).thenReturn(user);

        notificationService.schedule(request);

        ArgumentCaptor<ScheduledNotification> captor = ArgumentCaptor.forClass(ScheduledNotification.class);
        verify(scheduledNotificationRepository).save(captor.capture());
        ScheduledNotification savedNotification = captor.getValue();
        assertEquals(user.getId(), savedNotification.getUserId());
        assertEquals(request.isLitoranea(), savedNotification.isLitoranea());
        assertEquals(request.getCityName(), savedNotification.getCityName());
        assertEquals(request.getWhen(), savedNotification.getScheduledTime());
        assertFalse(savedNotification.isSent());
        assertFalse(savedNotification.isFailed());
    }
}
