package app.controller;

import app.dto.user.UserRegistrationRequestDto;
import app.dto.user.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "user manger",
        description = "Endpoints for managing members and managers in projects")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/me")
    @Operation(summary = "getting profile",
            description = "getting user's profile")
    public UserResponseDto getUserProfile() {
        return null;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/me")
    @Operation(summary = "updating profile",
            description = "updating user's profile")
    public UserResponseDto updateUserProfile(
            @RequestBody @Valid UserRegistrationRequestDto requestDto) {
        return null;
    }
}
