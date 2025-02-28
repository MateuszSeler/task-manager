package app.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentCreateRequestDto {
    @NotNull
    private Long taskId;
    @NotNull
    private Long userId;
    @NotBlank
    @Size(max = 2000)
    private String text;
}
