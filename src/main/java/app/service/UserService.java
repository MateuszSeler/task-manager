package app.service;

import app.dto.user.UserRegistrationRequestDto;
import app.dto.user.UserResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface UserService {
    UserResponseDto register(@Valid UserRegistrationRequestDto userRegistrationRequestDto);

    UserResponseDto getByEmail(@NotNull String email);

    UserResponseDto findById(@NotNull Long id);

    UserResponseDto updateProfile(Long userId, @Valid UserRegistrationRequestDto requestDto);

    void deletingById(@NotNull Long userId);

    UserResponseDto getById(@NotNull Long userId);
}
