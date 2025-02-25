package app.dto.project;

import app.dto.user.UserResponseDto;
import app.model.Project;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProjectDto {
    private Long id;
    @NotNull
    private String name;
    @Size(max = 1000)
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    @NotNull
    private Project.Status status;
    @NotNull
    private Set<UserResponseDto> projectManagers;
    @NotNull
    private Set<UserResponseDto> projectMembers;
}
