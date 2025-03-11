package app.config;

import app.service.attachment.DropBoxTokenService;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DropboxConfig {
    private final DropBoxTokenService dropBoxTokenService;

    @Bean
    public DbxClientV2 dropboxClient() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("spring-boot-dropbox").build();

        return new DbxClientV2(config, dropBoxTokenService.getNewAccessToken());
    }
}
