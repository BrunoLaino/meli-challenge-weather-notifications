package meli.challenge.weather.notifications.scheduleworker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityInfo {
    private String nome;
    private String uf;
    private String id;
}