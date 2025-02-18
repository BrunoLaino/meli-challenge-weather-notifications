package meli.challenge.weather.notifications.notificationsender.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessageDTO implements Serializable {
    private String scheduledNotificationId;
    private Long userId;
    private CityWeatherForecastDTO forecast;
    private WaveForecastDTO WaveForecast;
}