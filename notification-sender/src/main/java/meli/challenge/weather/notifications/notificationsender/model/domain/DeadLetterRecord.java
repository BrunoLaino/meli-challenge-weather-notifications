package meli.challenge.weather.notifications.notificationsender.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "dead_letter_records")
public class DeadLetterRecord {

    @Id
    private String id;

    private String payload;

    private String errorDetails;

    private LocalDateTime createdAt;
}
