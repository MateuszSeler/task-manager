package app.config;

import app.security.DropBoxTokenServiceImpl;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DropboxConfig {
    private final DropBoxTokenServiceImpl dropBoxTokenServiceImpl;

    @Bean
    public DbxClientV2 dropboxClient() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("spring-boot-dropbox").build();
        String apiName = "DropBox";

        return new DbxClientV2(config, dropBoxTokenServiceImpl.getValidToken(apiName));
    }
}
