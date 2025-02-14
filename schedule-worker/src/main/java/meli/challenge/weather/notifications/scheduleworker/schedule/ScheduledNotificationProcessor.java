package meli.challenge.weather.notifications.scheduleworker.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meli.challenge.weather.notifications.scheduleworker.deadletter.ProducerDeadLetterService;
import meli.challenge.weather.notifications.scheduleworker.model.domain.ScheduledNotification;
import meli.challenge.weather.notifications.scheduleworker.model.dto.CityWeatherForecast;
import meli.challenge.weather.notifications.scheduleworker.model.dto.NotificationMessage;
import meli.challenge.weather.notifications.scheduleworker.model.dto.WaveForecast;
import meli.challenge.weather.notifications.scheduleworker.repository.ScheduledNotificationRepository;
import meli.challenge.weather.notifications.scheduleworker.service.NotificationPublisherService;
import meli.challenge.weather.notifications.scheduleworker.service.WeatherService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledNotificationProcessor {

    private final ScheduledNotificationRepository scheduledRepo;
    private final WeatherService weatherService;
    private final ProducerDeadLetterService producerDeadLetterService;
    private final NotificationPublisherService notificationPublisherService;


    @Scheduled(fixedRate = 60000)
    public void processScheduledNotifications() {
        List<ScheduledNotification> scheduledNotifications = retrieveScheduledNotifications();
        log.info("Notificações agendadas encontradas: {}", scheduledNotifications);

        scheduledNotifications.forEach(this::processNotification);

        if (!scheduledNotifications.isEmpty()) {
            scheduledRepo.saveAll(scheduledNotifications);
        }
    }

    private List<ScheduledNotification> retrieveScheduledNotifications() {
        LocalDateTime now = LocalDateTime.now();
        return scheduledRepo.findByScheduledTimeLessThanEqualAndSentFalseAndFailedFalse(now);
    }

    private void processNotification(ScheduledNotification notification) {
        NotificationMessage notificationMessage = null;
        try {
            CityWeatherForecast forecast = retrieveCityForecast(notification);
            WaveForecast waveForecast = retrieveWaveForecast(notification);
            notificationMessage = buildNotificationMessage(notification, forecast, waveForecast);

            notificationPublisherService.publishNotification(notificationMessage);
            updateNotificationStatus(notification);
        } catch (Exception e) {
            log.error("Erro ao processar notificação agendada para {}: ", notification, e);
            notification.setFailed(true);

            if (ObjectUtils.isEmpty(notificationMessage)) {
                notificationMessage = buildMinimalNotificationMessage(notification);
            }
            producerDeadLetterService.sendToDeadLetter(notificationMessage, e);
        }
    }

    private static void updateNotificationStatus(ScheduledNotification notification) {
        notification.setSent(true);
    }

    private CityWeatherForecast retrieveCityForecast(ScheduledNotification notification) {
        return weatherService.getForecast(notification.getCityName(), notification.getCityId());
    }

    private WaveForecast retrieveWaveForecast(ScheduledNotification notification) {
        if (!notification.isLitoranea()) {
            return null;
        }
        String cityId = notification.getCityId();
        cityId = getCityIdIfHasText(notification, cityId);
        return weatherService.getWaveForecast(cityId);
    }

    private String getCityIdIfHasText(ScheduledNotification notification, String cityId) {
        if (!StringUtils.hasText(cityId)) {
            cityId = weatherService.discoverCityIdByName(notification.getCityName());
        }
        return cityId;
    }

    private NotificationMessage buildNotificationMessage(ScheduledNotification notification,
                                                         CityWeatherForecast forecast,
                                                         WaveForecast waveForecast) {
        NotificationMessage message = new NotificationMessage();
        message.setScheduledNotificationId(notification.getId());
        message.setUserId(notification.getUserId());
        message.setForecast(forecast);
        message.setWaveForecast(waveForecast);
        return message;
    }

    private NotificationMessage buildMinimalNotificationMessage(ScheduledNotification notification) {
        NotificationMessage message = new NotificationMessage();
        message.setScheduledNotificationId(notification.getId());
        message.setUserId(notification.getUserId());
        return message;
    }
}
