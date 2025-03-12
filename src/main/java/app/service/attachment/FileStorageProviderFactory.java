package app.service.attachment;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileStorageProviderFactory {
    private final Map<String, FileStorageProvider> providers;

    public FileStorageProvider getProvider(String apiName) {
        FileStorageProvider fileStorageProvider = providers.get(apiName.toUpperCase());
        if (fileStorageProvider == null) {
            throw new IllegalArgumentException("there is no FileStorageProvider named: " + apiName);
        }
        return fileStorageProvider;
    }
}
