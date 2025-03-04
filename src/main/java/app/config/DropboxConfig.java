package app.config;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DropboxConfig {

    @Value("${dropbox.token}")
    private String accessToken;

    @Bean
    public DbxClientV2 dropboxClient() {
        System.out.println("Dropbox token: " + accessToken);
        DbxRequestConfig config = DbxRequestConfig.newBuilder("spring-boot-dropbox").build();

        return new DbxClientV2(config, accessToken);
    }
}
