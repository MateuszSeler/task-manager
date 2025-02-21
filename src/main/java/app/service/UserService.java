package app.service;

import app.dto.user.UserRegistrationRequestDto;
import app.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto);

    UserResponseDto findByEmail(String email);

    UserResponseDto findById(Long id);
}
