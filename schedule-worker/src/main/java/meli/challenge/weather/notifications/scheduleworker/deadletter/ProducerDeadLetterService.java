package meli.challenge.weather.notifications.scheduleworker.deadletter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meli.challenge.weather.notifications.scheduleworker.config.RabbitConfig;
import meli.challenge.weather.notifications.scheduleworker.model.dto.DeadLetterRecordDTO;
import meli.challenge.weather.notifications.scheduleworker.model.dto.NotificationMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerDeadLetterService {

    private final RabbitTemplate rabbitTemplate;


    public void sendToDeadLetter(NotificationMessage message, Exception e) {
        log.error("Encaminhando mensagem para a DLQ do produtor. Motivo: {}. Mensagem: {}", e.getMessage(), message);

        DeadLetterRecordDTO dto = new DeadLetterRecordDTO(
                message.toString(),
                e.getMessage(),
                LocalDateTime.now()
        );

        rabbitTemplate.convertAndSend(
                RabbitConfig.DEAD_LETTER_EXCHANGE,
                RabbitConfig.DEAD_LETTER_ROUTING_KEY,
                dto
        );
    }
}
