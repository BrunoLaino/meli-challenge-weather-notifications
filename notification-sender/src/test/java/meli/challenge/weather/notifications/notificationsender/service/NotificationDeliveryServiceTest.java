package meli.challenge.weather.notifications.notificationsender.service;

import meli.challenge.weather.notifications.notificationsender.model.dto.NotificationMessageDTO;
import meli.challenge.weather.notifications.notificationsender.service.channel.NotificationChannel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class NotificationDeliveryServiceTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testDeliver_WithSupportedChannel() {
        NotificationChannel channel1 = mock(NotificationChannel.class);
        NotificationChannel channel2 = mock(NotificationChannel.class);
        NotificationMessageDTO message = new NotificationMessageDTO();

        when(channel1.supports(message)).thenReturn(false);
        when(channel2.supports(message)).thenReturn(true);

        List<NotificationChannel> channels = List.of(channel1, channel2);
        NotificationDeliveryService service = new NotificationDeliveryService(channels);

        service.deliver(message);

        verify(channel1, times(1)).supports(message);
        verify(channel2, times(1)).supports(message);
        verify(channel2, times(1)).send(message);
        verify(channel1, never()).send(message);
    }

    @Test
    void testDeliver_NoSupportedChannel() {
        NotificationChannel channel1 = mock(NotificationChannel.class);
        NotificationChannel channel2 = mock(NotificationChannel.class);
        NotificationMessageDTO message = new NotificationMessageDTO();

        when(channel1.supports(message)).thenReturn(false);
        when(channel2.supports(message)).thenReturn(false);

        List<NotificationChannel> channels = List.of(channel1, channel2);
        NotificationDeliveryService service = new NotificationDeliveryService(channels);

        service.deliver(message);

        verify(channel1, times(1)).supports(message);
        verify(channel2, times(1)).supports(message);
        verify(channel1, never()).send(message);
        verify(channel2, never()).send(message);

        String output = outContent.toString();
        assertTrue(output.contains("Nenhum canal suportou a mensagem: " + message));
    }
}
