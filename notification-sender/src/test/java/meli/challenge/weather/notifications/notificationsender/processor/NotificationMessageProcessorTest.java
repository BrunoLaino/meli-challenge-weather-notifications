package meli.challenge.weather.notifications.notificationsender.processor;

import meli.challenge.weather.notifications.notificationsender.deadletter.DeadLetterService;
import meli.challenge.weather.notifications.notificationsender.model.domain.User;
import meli.challenge.weather.notifications.notificationsender.model.dto.NotificationMessageDTO;
import meli.challenge.weather.notifications.notificationsender.repository.UserRepository;
import meli.challenge.weather.notifications.notificationsender.service.NotificationDeliveryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationMessageProcessorTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private NotificationDeliveryService deliveryService;

    @Mock
    private DeadLetterService deadLetterService;

    @InjectMocks
    private NotificationMessageProcessor processor;

    @Test
    void testProcess_Success() {
        NotificationMessageDTO message = new NotificationMessageDTO();
        message.setUserId(1L);

        User user = new User();
        user.setId(1L);
        user.setOptOut(false);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        processor.process(message);

        verify(deliveryService, times(1)).deliver(message);
        verify(deadLetterService, never()).sendToDeadLetter(any(), any());
    }

    @Test
    void testProcess_UserNotFound() {
        NotificationMessageDTO message = new NotificationMessageDTO();
        message.setUserId(2L);
        when(userRepo.findById(2L)).thenReturn(Optional.empty());

        processor.process(message);

        ArgumentCaptor<ResponseStatusException> exceptionCaptor = ArgumentCaptor.forClass(ResponseStatusException.class);
        verify(deadLetterService, times(1)).sendToDeadLetter(eq(message), exceptionCaptor.capture());
        ResponseStatusException ex = exceptionCaptor.getValue();
        assertEquals("Usuário 2 não encontrado", ex.getReason());
        verify(deliveryService, never()).deliver(any());
    }

    @Test
    void testProcess_UserOptOut() {
        NotificationMessageDTO message = new NotificationMessageDTO();
        message.setUserId(3L);

        User user = new User();
        user.setId(3L);
        user.setOptOut(true);
        when(userRepo.findById(3L)).thenReturn(Optional.of(user));

        processor.process(message);

        ArgumentCaptor<ResponseStatusException> exceptionCaptor = ArgumentCaptor.forClass(ResponseStatusException.class);
        verify(deadLetterService, times(1)).sendToDeadLetter(eq(message), exceptionCaptor.capture());
        ResponseStatusException ex = exceptionCaptor.getValue();
        assertEquals("Usuário ID: 3 está em opt-out.", ex.getReason());
        verify(deliveryService, never()).deliver(any());
    }
}
