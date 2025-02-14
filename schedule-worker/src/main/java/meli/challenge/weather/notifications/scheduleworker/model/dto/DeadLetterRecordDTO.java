package meli.challenge.weather.notifications.scheduleworker.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeadLetterRecordDTO {

    private String payload;
    private String errorDetails;
    private LocalDateTime createdAt;
}