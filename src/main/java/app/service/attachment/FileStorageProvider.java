package app.service.attachment;

import app.dto.attachment.ExternalAttachmentResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageProvider {
    ExternalAttachmentResponseDto uploadFile(MultipartFile file);

    byte[] downloadFile(String fileId);

    void deleteFile(String fileId);
}
