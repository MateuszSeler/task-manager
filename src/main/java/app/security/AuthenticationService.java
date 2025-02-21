package app.security;

import app.dto.user.UserLoginRequestDto;
import app.dto.user.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authenticate(UserLoginRequestDto userLoginRequestDto);
}
