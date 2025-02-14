package meli.challenge.weather.notifications.scheduleworker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaveForecast  implements Serializable {
    private String cityName;
    private String uf;
    private WavePeriod manha;
    private WavePeriod tarde;
    private WavePeriod noite;
}