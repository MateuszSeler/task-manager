package app.service.attachment;

import app.dto.attachment.AttachmentResponseDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    AttachmentResponseDto uploadFile(
            @NotNull Long taskId, MultipartFile file, @NotNull String apiName);

    byte[] downloadFile(@NotNull Long attachmentId);

    void deleteAttachmentById(@NotNull Long attachmentId);
}
