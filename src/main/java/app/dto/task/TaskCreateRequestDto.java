package app.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TaskCreateRequestDto {
    @NotBlank
    private String name;
    @Size(max = 1000)
    private String description;
    @NotNull
    private String priority;
    private LocalDate dueDate;
    @NotNull
    private Long projectId;
    private Long assigneeId;
}
