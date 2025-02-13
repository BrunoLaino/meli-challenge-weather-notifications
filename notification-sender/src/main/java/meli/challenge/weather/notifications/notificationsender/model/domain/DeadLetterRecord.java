package meli.challenge.weather.notifications.notificationsender.model.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "dead_letter_records")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeadLetterRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Column(name = "error_details", columnDefinition = "TEXT")
    private String errorDetails;

    private LocalDateTime createdAt;
}