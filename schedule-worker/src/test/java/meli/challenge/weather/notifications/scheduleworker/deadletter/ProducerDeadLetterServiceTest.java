package meli.challenge.weather.notifications.scheduleworker.deadletter;
import meli.challenge.weather.notifications.scheduleworker.config.RabbitConfig;
import meli.challenge.weather.notifications.scheduleworker.deadletter.ProducerDeadLetterService;
import meli.challenge.weather.notifications.scheduleworker.model.dto.DeadLetterRecordDTO;
import meli.challenge.weather.notifications.scheduleworker.model.dto.NotificationMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProducerDeadLetterServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ProducerDeadLetterService producerDeadLetterService;

    @Test
    void testSendToDeadLetter() {
        NotificationMessage message = new NotificationMessage();
        Exception exception = new Exception("Test exception");

        producerDeadLetterService.sendToDeadLetter(message, exception);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitConfig.DEAD_LETTER_EXCHANGE),
                eq(RabbitConfig.DEAD_LETTER_ROUTING_KEY),
                captor.capture()
        );

        Object captured = captor.getValue();
        assertTrue(captured instanceof DeadLetterRecordDTO);
        DeadLetterRecordDTO dto = (DeadLetterRecordDTO) captured;
        assertEquals(message.toString(), dto.getPayload());
        assertEquals("Test exception", dto.getErrorDetails());
        assertNotNull(dto.getCreatedAt());
        assertTrue(dto.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
