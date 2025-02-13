package meli.challenge.weather.notifications.apinotifications.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequestDTO {
    private Long userId;
    private LocalDateTime when;
    private String cityName;
    private String cityId;
    private boolean litoranea;
}