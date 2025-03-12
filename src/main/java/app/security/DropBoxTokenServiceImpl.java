package app.security;

import app.dto.token.ExternalTokenDto;
import app.model.Token;
import app.repository.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DropBoxTokenServiceImpl {
    private final TokenRepository tokenRepository;
    private final ObjectMapper mapper;
    private final StringEncryptor encryptor;

    @Value("${dropbox.token}")
    private String refreshToken;
    @Value("${dropbox.client_id}")
    private String clientId;
    @Value("${dropbox.client_secret}")
    private String clientSecret;
    @Value("${dropbox.token_url}")
    private String tokenUrl;

    public String getValidToken(String apiName) {
        Token token = getOrCreate(apiName);

        if (token.getExpirationTime() == null
                || Instant.now().isBefore(token.getExpirationTime())) {

            ExternalTokenDto newAccessToken = getNewAccessToken();
            token
                    .setToken(encryptor.encrypt(newAccessToken.accessToken()))
                    .setExpirationTime(Instant.now().plusSeconds(newAccessToken.expiresIn()));

            tokenRepository.save((token));
        }

        return encryptor.decrypt(token.getToken());
    }

    private Token getOrCreate(String apiName) {
        Optional<Token> optionalToken = tokenRepository.findByApiName(apiName);

        if (optionalToken.isEmpty()) {
            return new Token().setApiName(apiName);
        }

        return optionalToken.get();
    }

    private ExternalTokenDto getNewAccessToken() {
        HttpClient httpClient = HttpClient.newHttpClient();

        String formData = "grant_type=refresh_token"
                + "&refresh_token=" + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8)
                + "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
                + "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tokenUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        try {
            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            ExternalTokenDto externalTokenDto =
                    mapper.readValue(response.body(), ExternalTokenDto.class);

            return externalTokenDto;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Getting new token from Dropbox api failed", e);
        }
    }
}
