package app.service.attachment;

import app.dto.attachment.AttachmentResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    AttachmentResponseDto uploadFile(Long taskId, MultipartFile file);

    byte[] downloadFile(@NotNull Long attachmentId);

    void deleteLabelById(@Valid Long attachmentId);
}
