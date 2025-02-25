package app.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProjectCreateRequestDto {
    @NotBlank
    private String name;
    @Size(max = 1000)
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}
