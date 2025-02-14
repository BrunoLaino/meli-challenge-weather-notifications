package meli.challenge.weather.notifications.apinotifications.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import meli.challenge.weather.notifications.apinotifications.model.domain.User;
import meli.challenge.weather.notifications.apinotifications.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testOptOut() throws Exception {
        mockMvc.perform(post("/notifications/users/1/opt-out"))
                .andExpect(status().isNoContent());
        verify(userService).optOutUser(1L);
    }

    @Test
    void testOptIn() throws Exception {
        mockMvc.perform(post("/notifications/users/1/opt-in"))
                .andExpect(status().isNoContent());
        verify(userService).optInUser(1L);
    }

    @Test
    void testCreateOrUpdate() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setOptOut(false);
        when(userService.createOrUpdate(any(User.class))).thenReturn(user);
        String jsonRequest = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/notifications/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonRequest));
        verify(userService).createOrUpdate(any(User.class));
    }
}
