package app.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import app.dto.user.UserLoginRequestDto;
import app.exception.AuthenticationException;
import app.model.User;
import app.repository.user.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void authenticate_nonExistingUser_authenticationException() {
        UserLoginRequestDto loginRequestDto = new UserLoginRequestDto()
                .setEmail("nonExistingUser@gmail.com")
                .setPassword("haslojana");

        Mockito.when(userRepository.findByEmail(loginRequestDto.getEmail()))
                .thenReturn(Optional.empty());

        AuthenticationException exception =
                assertThrows(AuthenticationException.class,
                        () -> authenticationService.authenticate(loginRequestDto));

        assertEquals("User with email: " + loginRequestDto.getEmail()
                + " not found", exception.getMessage());

    }

    @Test
    void authenticate_wrongPassword_authenticationException() {
        UserLoginRequestDto loginRequestDto = new UserLoginRequestDto()
                .setEmail("jan@gmail.com")
                .setPassword("wrongPassword");

        User user = new User()
                .setId(1L)
                .setEmail("jan@gmail.com")
                .setPassword("haslojana")
                .setFirstName("Jan")
                .setLastName("Nowak");

        Mockito.when(userRepository.findByEmail(loginRequestDto.getEmail()))
                .thenReturn(Optional.of(user));

        Mockito.when(authenticationManager
                        .authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Incorrect password"));

        AuthenticationException exception =
                assertThrows(AuthenticationException.class,
                        () -> authenticationService.authenticate(loginRequestDto));

        assertEquals("Invalid password", exception.getMessage());
    }
}
