package meli.challenge.weather.notifications.scheduleworker.schedule;

import meli.challenge.weather.notifications.scheduleworker.deadletter.ProducerDeadLetterService;
import meli.challenge.weather.notifications.scheduleworker.model.domain.ScheduledNotification;
import meli.challenge.weather.notifications.scheduleworker.model.dto.CityWeatherForecast;
import meli.challenge.weather.notifications.scheduleworker.model.dto.NotificationMessage;
import meli.challenge.weather.notifications.scheduleworker.repository.ScheduledNotificationRepository;
import meli.challenge.weather.notifications.scheduleworker.schedule.ScheduledNotificationProcessor;
import meli.challenge.weather.notifications.scheduleworker.service.NotificationPublisherService;
import meli.challenge.weather.notifications.scheduleworker.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduledNotificationProcessorTest {

    @Mock
    private ScheduledNotificationRepository scheduledRepo;

    @Mock
    private WeatherService weatherService;

    @Mock
    private ProducerDeadLetterService producerDeadLetterService;

    @Mock
    private NotificationPublisherService notificationPublisherService;

    @InjectMocks
    private ScheduledNotificationProcessor processor;

    @Test
    void testProcessScheduledNotificationsSuccess() {
        ScheduledNotification notification = new ScheduledNotification();
        notification.setId(1L);
        notification.setUserId(1L);
        notification.setCityName("São Paulo");
        notification.setCityId("12345");
        notification.setLitoranea(false);
        notification.setScheduledTime(LocalDateTime.now().minusMinutes(1));
        notification.setSent(false);
        notification.setFailed(false);

        when(scheduledRepo.findByScheduledTimeLessThanEqualAndSentFalseAndFailedFalse(any(LocalDateTime.class)))
                .thenReturn(List.of(notification));

        CityWeatherForecast forecast = new CityWeatherForecast();
        forecast.setCityName("São Paulo");
        forecast.setUf("SP");
        when(weatherService.getForecast("São Paulo", "12345")).thenReturn(forecast);

        processor.processScheduledNotifications();

        verify(notificationPublisherService).publishNotification(argThat(msg ->
                msg.getScheduledNotificationId().equals(1L)
                        && msg.getUserId().equals(1L)
                        && msg.getForecast() == forecast
                        && msg.getWaveForecast() == null
        ));

        verify(scheduledRepo).saveAll(List.of(notification));
        assertTrue(notification.isSent());
        assertFalse(notification.isFailed());
    }

    @Test
    void testProcessScheduledNotificationsFailure() {
        ScheduledNotification notification = new ScheduledNotification();
        notification.setId(2L);
        notification.setUserId(2L);
        notification.setCityName("Rio de Janeiro");
        notification.setCityId("67890");
        notification.setLitoranea(true);
        notification.setScheduledTime(LocalDateTime.now().minusMinutes(1));
        notification.setSent(false);
        notification.setFailed(false);

        when(scheduledRepo.findByScheduledTimeLessThanEqualAndSentFalseAndFailedFalse(any(LocalDateTime.class)))
                .thenReturn(List.of(notification));

        RuntimeException forecastException = new RuntimeException("Forecast error");
        when(weatherService.getForecast("Rio de Janeiro", "67890")).thenThrow(forecastException);

        processor.processScheduledNotifications();

        verify(producerDeadLetterService).sendToDeadLetter(argThat(msg ->
                msg.getScheduledNotificationId().equals(2L)
                        && msg.getUserId().equals(2L)
                        && msg.getForecast() == null
                        && msg.getWaveForecast() == null
        ), eq(forecastException));

        verify(notificationPublisherService, never()).publishNotification(any(NotificationMessage.class));
        verify(scheduledRepo).saveAll(List.of(notification));
        assertTrue(notification.isFailed());
        assertFalse(notification.isSent());
    }
}
