package app.dto.token;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DropboxTokenResponseDto (
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") int expiresIn) {
}
