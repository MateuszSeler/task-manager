package app.service.attachment;

import app.dto.attachment.ExternalAttachmentResponseDto;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.sharing.ListSharedLinksResult;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileStorageProviderDropboxImpl implements FileStorageProvider {
    private final DbxClientV2 dropboxClient;

    public ExternalAttachmentResponseDto uploadFile(MultipartFile file) {
        String dropboxPath = "/" + file.getOriginalFilename();

        try (InputStream inputStream = file.getInputStream()) {
            FileMetadata metadata = dropboxClient.files()
                    .uploadBuilder(dropboxPath)
                    .uploadAndFinish(inputStream);

            return new ExternalAttachmentResponseDto(
                    metadata.getId(),
                    metadata.getPathLower(),
                    getLink(metadata.getPathLower()),
                    metadata.getName(),
                    Instant.now());
        } catch (IOException | DbxException e) {
            throw new RuntimeException("Uploading file to Dropbox failed", e);
        }
    }

    public byte[] downloadFile(String filePath) {
        try (InputStream inputStream = dropboxClient.files()
                .download(filePath)
                .getInputStream()) {
            return inputStream.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Downloading file from Dropbox failed: ", e);
        }
    }

    public void deleteFile(String filePath) {
        try {
            dropboxClient.files().deleteV2(filePath);
        } catch (Exception e) {
            throw new RuntimeException("Deleting file from Dropbox failed: ", e);
        }
    }

    private String getLink(String filePath) {
        try {
            ListSharedLinksResult linksResult = dropboxClient.sharing().listSharedLinksBuilder()
                    .withPath(filePath)
                    .withDirectOnly(true)
                    .start();

            if (!linksResult.getLinks().isEmpty()) {
                return linksResult
                        .getLinks()
                        .get(0)
                        .getUrl()
                        .replace("?dl=0", "?raw=1");
            }

            SharedLinkMetadata sharedLink = dropboxClient
                    .sharing()
                    .createSharedLinkWithSettings(filePath);
            return sharedLink.getUrl().replace("?dl=0", "?raw=1");

        } catch (DbxException e) {
            throw new RuntimeException("Failed to generate link", e);
        }
    }
}
