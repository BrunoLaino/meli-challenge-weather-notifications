package meli.challenge.weather.notifications.notificationsender.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WavePeriodDTO implements Serializable {
    private String agitacao;
    private String altura;
    private String direcao;
    private String vento;
    private String ventoDir;
}