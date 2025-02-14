package meli.challenge.weather.notifications.notificationsender.consumer;

import meli.challenge.weather.notifications.notificationsender.model.domain.DeadLetterRecord;
import meli.challenge.weather.notifications.notificationsender.model.dto.DeadLetterRecordDTO;
import meli.challenge.weather.notifications.notificationsender.repository.DeadLetterRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeadLetterConsumerTest {

    @Mock
    private DeadLetterRecordRepository deadLetterRecordRepository;

    @Mock
    private Jackson2JsonMessageConverter messageConverter;

    @InjectMocks
    private DeadLetterConsumer deadLetterConsumer;

    @Test
    void testConsumeDeadLetter() {
        LocalDateTime now = LocalDateTime.now();
        DeadLetterRecordDTO dto = new DeadLetterRecordDTO("test payload", "test error details", now);

        deadLetterConsumer.consumeDeadLetter(dto);

        ArgumentCaptor<DeadLetterRecord> recordCaptor = ArgumentCaptor.forClass(DeadLetterRecord.class);
        verify(deadLetterRecordRepository).save(recordCaptor.capture());

        DeadLetterRecord record = recordCaptor.getValue();
        assertEquals("test payload", record.getPayload());
        assertEquals("test error details", record.getErrorDetails());
        assertEquals(now, record.getCreatedAt());
    }
}
