package app.dto.attachment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AttachmentResponseDto {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Long taskId;
    @NotNull
    private String fileId;
    @NotBlank
    private String fileUrl;
    @NotNull
    private String filePath;
    @NotNull
    private Instant uploadDate;
}
