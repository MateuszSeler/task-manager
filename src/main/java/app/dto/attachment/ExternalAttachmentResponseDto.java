package app.dto.attachment;

import java.time.Instant;

public record ExternalAttachmentResponseDto(
        String fileId,
        String filePath,
        String fileUrl,
        String fileName,
        Instant uploadDate) {
}
