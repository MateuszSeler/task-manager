package app.service;

import app.dto.project.ProjectCreateRequestDto;
import app.dto.project.ProjectDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public interface ProjectService {
    ProjectDto createProject(@NotNull Long userId,
                             @Valid ProjectCreateRequestDto requestDto);

    Set<ProjectDto> getUsersProjects(@NotNull Long userId);

    ProjectDto getProjectById(@NotNull Long projectId);

    ProjectDto updateProjectById(@NotNull Long projectId,
                                 @Valid ProjectCreateRequestDto requestDto);

    void deleteById(@NotNull Long projectId);
}
