package meli.challenge.weather.notifications.notificationsender.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meli.challenge.weather.notifications.notificationsender.config.RabbitConfig;
import meli.challenge.weather.notifications.notificationsender.model.domain.DeadLetterRecord;
import meli.challenge.weather.notifications.notificationsender.model.dto.DeadLetterRecordDTO;
import meli.challenge.weather.notifications.notificationsender.repository.DeadLetterRecordRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeadLetterConsumer {

    private final DeadLetterRecordRepository deadLetterRecordRepository;
    private final Jackson2JsonMessageConverter messageConverter;

    @RabbitListener(queues = RabbitConfig.DEAD_LETTER_QUEUE)
    public void consumeDeadLetter(DeadLetterRecordDTO dto) {
        try {
            DeadLetterRecord record = new DeadLetterRecord();
            record.setPayload(dto.getPayload());
            record.setErrorDetails(dto.getErrorDetails());
            record.setCreatedAt(dto.getCreatedAt());

            deadLetterRecordRepository.save(record);
            log.info("Mensagem da DLQ salva no banco: {}", record);
        } catch (Exception e) {
            log.error("Erro ao processar mensagem da DLQ", e);
        }
    }
}
