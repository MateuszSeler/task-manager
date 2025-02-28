package app.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentDto {
    private Long id;
    @NotNull
    private Long taskId;
    @NotNull
    private Long userId;
    @NotBlank
    @Size(max = 2000)
    private String text;
    @NotNull
    private LocalDateTime timestamp;
}
