package app.service;

import app.dto.user.UserRegistrationRequestDto;
import app.dto.user.UserResponseDto;
import jakarta.validation.Valid;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto);

    UserResponseDto getByEmail(String email);

    UserResponseDto findById(Long id);

    UserResponseDto updateProfile(Long userId, @Valid UserRegistrationRequestDto requestDto);

    void deletingById(Long userId);

    UserResponseDto getById(@Valid Long userId);
}
