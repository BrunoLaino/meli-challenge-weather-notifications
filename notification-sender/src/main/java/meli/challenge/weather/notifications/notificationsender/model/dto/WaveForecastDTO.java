package meli.challenge.weather.notifications.notificationsender.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaveForecastDTO implements Serializable {
    private String cityName;
    private String uf;
    private WavePeriodDTO manha;
    private WavePeriodDTO tarde;
    private WavePeriodDTO noite;
}