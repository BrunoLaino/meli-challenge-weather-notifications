package meli.challenge.weather.notifications.scheduleworker.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Tabela de agendamentos. Assumimos que armazenamos:
 * - cityName (ex.: "Rio de Janeiro")
 * - cityId (ex.: "241") -> pode ser null se ainda não descobrimos
 * - isLitoranea (se true, buscamos ondas)
 * - scheduledTime (quando enviar)
 * - sent (se já foi enviado)
 */
@Entity
@Table(name = "scheduled_notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ScheduledNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String cityName;
    private String cityId;
    private boolean litoranea;
    private LocalDateTime scheduledTime;
    private boolean sent;
    private boolean failed;

}
