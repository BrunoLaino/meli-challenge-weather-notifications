package meli.challenge.weather.notifications.notificationsender.consumer;

import meli.challenge.weather.notifications.notificationsender.model.dto.NotificationMessageDTO;
import meli.challenge.weather.notifications.notificationsender.processor.NotificationMessageProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SendNotificationConsumerTest {

    @Mock
    private NotificationMessageProcessor messageProcessor;

    @InjectMocks
    private SendNotificationConsumer consumer;

    @Test
    void testConsumeSuccess() {
        NotificationMessageDTO message = new NotificationMessageDTO();
        consumer.consume(message);
        verify(messageProcessor).process(message);
    }

    @Test
    void testConsumeFailure() {
        NotificationMessageDTO message = new NotificationMessageDTO();
        RuntimeException exception = new RuntimeException("Test exception");
        doThrow(exception).when(messageProcessor).process(message);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> consumer.consume(message));
        assertEquals("Test exception", thrown.getMessage());
        verify(messageProcessor).process(message);
    }
}
