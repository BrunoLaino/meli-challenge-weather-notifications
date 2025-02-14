package meli.challenge.weather.notifications.scheduleworker.model.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityWeatherForecast implements Serializable {
    private String cityName;
    private String uf;
    private List<DailyForecast> dailyForecasts = new ArrayList<>();
}