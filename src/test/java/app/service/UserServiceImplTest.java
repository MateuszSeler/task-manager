package app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import app.dto.user.UserRegistrationRequestDto;
import app.exception.EntityNotFoundException;
import app.exception.RegistrationException;
import app.model.User;
import app.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void register_userAlreadyExisted_registrationException() {
        UserRegistrationRequestDto registrationRequestDto = new UserRegistrationRequestDto()
                .setEmail("jan@gmail.com")
                .setPassword("haslojana")
                .setRepeatPassword("haslojana")
                .setFirstName("Jan")
                .setLastName("Nowak");

        User user = new User()
                .setId(1L)
                .setEmail("jan@gmail.com")
                .setPassword("haslojana")
                .setFirstName("Jan")
                .setLastName("Nowak");

        Mockito.when(userRepository.findByEmail(registrationRequestDto.getEmail()))
                .thenReturn(Optional.of(user));

        RegistrationException exception =
                assertThrows(RegistrationException.class,
                        () -> userService.register(registrationRequestDto));

        assertEquals("User with email: " + registrationRequestDto.getEmail()
                + " not found", exception.getMessage());
    }

    @Test
    void register_passwordsDoNotMatch_registrationException() {
        UserRegistrationRequestDto registrationRequestDto = new UserRegistrationRequestDto()
                .setEmail("jan@gmail.com")
                .setPassword("haslojana")
                .setRepeatPassword("wrongRepeatPassword")
                .setFirstName("Jan")
                .setLastName("Nowak");

        Mockito.when(userRepository.findByEmail(registrationRequestDto.getEmail()))
                .thenReturn(Optional.empty());

        RegistrationException exception =
                assertThrows(RegistrationException.class,
                        () -> userService.register(registrationRequestDto));

        assertEquals("Passwords do not match", exception.getMessage());
    }

    @Test
    void getByEmail_NotExistingUser_entityNotFoundException() {
        String email = "nonExistingUser@gmail.com";

        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> userService.getByEmail(email));

        assertEquals("User with email: " + email
                + " not found", exception.getMessage());
    }

    @Test
    void findById_NotExistingUser_entityNotFoundException() {
        Long userId = 31L;

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> userService.findById(userId));

        assertEquals("User with id: " + userId
                + " not found", exception.getMessage());
    }
}
