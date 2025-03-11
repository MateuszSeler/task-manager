package app.service.attachment;

import app.dto.token.DropboxTokenResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DropBoxTokenService {
    private final ObjectMapper mapper;

    @Value("${dropbox.token}")
    private String refreshToken;
    @Value("${dropbox.client_id}")
    private String clientId;
    @Value("${dropbox.client_secret}")
    private String clientSecret;
    @Value("${dropbox.token_url}")
    private String tokenUrl;

    public String getNewAccessToken() {
        HttpClient httpClient = HttpClient.newHttpClient();

        String formData = "grant_type=refresh_token" +
                "&refresh_token=" + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8) +
                "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tokenUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        try {
            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            DropboxTokenResponseDto dropboxTokenResponseDto =
                    mapper.readValue(response.body(), DropboxTokenResponseDto.class);

            return dropboxTokenResponseDto.accessToken();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Getting new token from Dropbox api failed", e);
        }
    }
}
