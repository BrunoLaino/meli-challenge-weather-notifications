package meli.challenge.weather.notifications.notificationsender.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeadLetterRecordDTO implements Serializable {

    private String payload;
    private String errorDetails;
    private LocalDateTime createdAt;
}