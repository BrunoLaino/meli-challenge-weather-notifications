package meli.challenge.weather.notifications.apinotifications.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import meli.challenge.weather.notifications.apinotifications.model.dto.ScheduleRequestDTO;
import meli.challenge.weather.notifications.apinotifications.service.NotificationService;
import meli.challenge.weather.notifications.apinotifications.controller.NotificationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testScheduleNotification_ReturnsAccepted() throws Exception {
        ScheduleRequestDTO request = new ScheduleRequestDTO();
        request.setUserId(1L);
        request.setLitoranea(true);
        request.setCityName("Test City");
        request.setWhen(LocalDateTime.of(2025, 2, 15, 10, 0));

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/notifications/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isAccepted());

        verify(notificationService).schedule(any(ScheduleRequestDTO.class));
    }
}
