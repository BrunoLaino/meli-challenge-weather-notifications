package meli.challenge.weather.notifications.notificationsender.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityWeatherForecastDTO implements Serializable {
    private String cityName;
    private String uf;
    private List<DailyForecastDTO> dailyForecasts = new ArrayList<>();
}