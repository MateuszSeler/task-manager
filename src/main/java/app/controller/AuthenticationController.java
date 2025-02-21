package app.controller;

import app.dto.user.UserLoginRequestDto;
import app.dto.user.UserLoginResponseDto;
import app.dto.user.UserRegistrationRequestDto;
import app.dto.user.UserResponseDto;
import app.security.AuthenticationService;
import app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "authentication manger",
        description = "Endpoints for authentication users in book shop app")
@RequiredArgsConstructor
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("registration")
    @Operation(summary = "registration", description = "registration of new user")
    public ResponseEntity<UserResponseDto> register(
            @RequestBody @Valid UserRegistrationRequestDto userRegistrationRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.register(userRegistrationRequestDto));
    }

    @PostMapping("login")
    @Operation(summary = "login", description = "login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto) {
        return authenticationService.authenticate(userLoginRequestDto);
    }
}
