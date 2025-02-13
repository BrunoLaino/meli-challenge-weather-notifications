package meli.challenge.weather.notifications.notificationsender.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityInfoDTO {
    private String nome;
    private String uf;
    private String id;
}