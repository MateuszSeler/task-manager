package app.security;

import app.dto.user.UserLoginRequestDto;
import app.dto.user.UserLoginResponseDto;
import app.exception.AuthenticationException;
import app.model.User;
import app.repository.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public UserLoginResponseDto authenticate(UserLoginRequestDto userLoginRequestDto) {
        Optional<User> optionalUserByEmail =
                userRepository.findByEmail(userLoginRequestDto.getEmail());

        if (optionalUserByEmail.isEmpty()) {
            throw new AuthenticationException("User with email: "
                    + userLoginRequestDto.getEmail() + " not found");
        }

        try {
            Authentication authenticate =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            userLoginRequestDto.getEmail(),
                            userLoginRequestDto.getPassword()));
            String token = jwtUtil.generateToken(authenticate.getName());

            return new UserLoginResponseDto(token);
        } catch (BadCredentialsException exception) {
            throw new AuthenticationException("Invalid password");
        }
    }
}
