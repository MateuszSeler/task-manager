package app.controller;

import app.dto.user.UserRegistrationRequestDto;
import app.dto.user.UserResponseDto;
import app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "user manger",
        description = "Endpoints for managing members and managers in projects")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/me")
    @Operation(summary = "getting profile",
            description = "getting own user's profile")
    public UserResponseDto getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getByEmail(authentication.getName());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/me")
    @Operation(summary = "updating profile",
            description = "updating own user's profile")
    public UserResponseDto updateProfile(
            @RequestBody @Valid UserRegistrationRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.updateProfile(
                userService.getByEmail(authentication.getName()).getId(), requestDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{userId}")
    @Operation(summary = "getting user",
            description = "getting user by id")
    public UserResponseDto getUserById(@PathVariable @NotNull Long userId) {
        return userService.getById(userId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "deleting user",
            description = "deleting user by id")
    public void deletingUserById(@PathVariable @NotNull Long userId) {
        userService.deletingById(userId);
    }
}
