package meli.challenge.weather.notifications.scheduleworker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage implements Serializable {
    private Long scheduledNotificationId;
    private Long userId;
    private CityWeatherForecast forecast;
    private WaveForecast waveForecast;
}