package meli.challenge.weather.notifications.scheduleworker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyForecast implements Serializable {
    private LocalDate date;
    private String weather;
    private int minTemp;
    private int maxTemp;
}