package meli.challenge.weather.notifications.notificationsender.deadletter;

import meli.challenge.weather.notifications.notificationsender.config.RabbitConfig;
import meli.challenge.weather.notifications.notificationsender.deadletter.DeadLetterService;
import meli.challenge.weather.notifications.notificationsender.model.dto.DeadLetterRecordDTO;
import meli.challenge.weather.notifications.notificationsender.model.dto.NotificationMessageDTO;
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
class DeadLetterServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private DeadLetterService deadLetterService;

    @Test
    void testSendToDeadLetter() {
        NotificationMessageDTO message = new NotificationMessageDTO();
        Exception exception = new Exception("Test exception");

        deadLetterService.sendToDeadLetter(message, exception);

        ArgumentCaptor<Object> dtoCaptor = ArgumentCaptor.forClass(Object.class);
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitConfig.DEAD_LETTER_EXCHANGE),
                eq(RabbitConfig.DEAD_LETTER_ROUTING_KEY),
                dtoCaptor.capture()
        );

        Object captured = dtoCaptor.getValue();
        assertTrue(captured instanceof DeadLetterRecordDTO);
        DeadLetterRecordDTO dto = (DeadLetterRecordDTO) captured;
        assertEquals(message.toString(), dto.getPayload());
        assertEquals("Test exception", dto.getErrorDetails());

        LocalDateTime now = LocalDateTime.now();
        assertTrue(dto.getCreatedAt().isBefore(now.plusSeconds(1)));
        assertTrue(dto.getCreatedAt().isAfter(now.minusSeconds(1)));
    }
}
