package meli.challenge.weather.notifications.apinotifications.service;

import meli.challenge.weather.notifications.apinotifications.model.domain.User;
import meli.challenge.weather.notifications.apinotifications.model.dto.ScheduleRequestDTO;
import meli.challenge.weather.notifications.apinotifications.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetUserSuccess() {
        ScheduleRequestDTO request = new ScheduleRequestDTO();
        request.setUserId(1L);

        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUser(request);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetUserNotFound() {
        ScheduleRequestDTO request = new ScheduleRequestDTO();
        request.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userService.getUser(request)
        );
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Usuário não encontrado: 1", exception.getReason());
    }

    @Test
    void testOptOutUserSuccess() {
        User user = new User();
        user.setId(1L);
        user.setOptOut(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.optOutUser(1L);
        assertTrue(result.isOptOut());
    }

    @Test
    void testOptOutUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userService.optOutUser(1L)
        );
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Usuário não encontrado", exception.getReason());
    }

    @Test
    void testOptInUserSuccess() {
        User user = new User();
        user.setId(1L);
        user.setOptOut(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.optInUser(1L);
        assertFalse(result.isOptOut());
    }

    @Test
    void testOptInUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userService.optInUser(1L)
        );
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Usuário não encontrado", exception.getReason());
    }

    @Test
    void testCreateOrUpdate() {
        User user = new User();
        user.setId(1L);
        user.setOptOut(false);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createOrUpdate(user);
        assertEquals(user, result);
    }
}
