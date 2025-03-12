package app.dto.token;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class TokenDto {
    private Long id;
    @NotBlank
    private String apiName;
    @NotBlank
    private String token;
    @NotNull
    private Instant expirationTime;
}
