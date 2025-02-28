package app.service;

import app.dto.project.ProjectCreateRequestDto;
import app.dto.project.ProjectDto;
import app.exception.DataProcessingException;
import app.exception.EntityNotFoundException;
import app.mapper.ProjectMapper;
import app.model.Project;
import app.model.Task;
import app.model.User;
import app.repository.ProjectRepository;
import app.repository.TaskRepository;
import app.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    public ProjectDto createProject(
            @NotNull Long userId, @Valid ProjectCreateRequestDto requestDto) {
        Project newProject = projectMapper.toModel(requestDto);
        User owner = getUserByIdOrThrowEntityNotFoundException(userId);

        newProject.setStatus(Project.Status.INITIATED);
        newProject.getProjectManagers().add(owner);
        newProject.getProjectMembers().add(owner);

        if (requestDto.getStartDate().compareTo(requestDto.getEndDate()) > 0) {
            throw new DataProcessingException("EndDate should be placed after StartDate");
        }

        return projectMapper.toDto(projectRepository.save(newProject));
    }

    @Override
    public Set<ProjectDto> getUsersProjects(@NotNull Long userId) {
        getUserByIdOrThrowEntityNotFoundException(userId);

        if (projectRepository.findUsersProjectsWithNoMembersNoManagers(userId) == null
                || projectRepository.findUsersProjectsWithNoMembersNoManagers(userId).isEmpty()) {
            return new HashSet<>();
        }

        return projectRepository.findUsersProjectsWithNoMembersNoManagers(userId)
                .stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public ProjectDto getProjectById(Long projectId) {
        return projectMapper.toDto(getProjectByIdOrThrowEntityNotFoundException(projectId));
    }

    @Override
    public ProjectDto updateProjectById(
            @NotNull Long projectId, @Valid ProjectCreateRequestDto requestDto) {
        Project updatedProject = getProjectByIdOrThrowEntityNotFoundException(projectId)
                .setName(requestDto.getName())
                .setDescription(requestDto.getDescription())
                .setStartDate(requestDto.getStartDate())
                .setEndDate(requestDto.getEndDate());
        return projectMapper.toDto(projectRepository.save(updatedProject));
    }

    @Override
    public void deleteById(@NotNull Long projectId) {
        getProjectByIdOrThrowEntityNotFoundException(projectId);

        Set<Task> tasksFromProject = taskRepository
                .getTasksFromProjectWithNoUserNoProjectNoLabels(projectId);
        for (Task task : tasksFromProject) {
            taskRepository.deleteById(task.getId());
        }

        projectRepository.deleteById(projectId);
    }

    private User getUserByIdOrThrowEntityNotFoundException(@NotNull Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + userId + " not found"));
    }

    private Project getProjectByIdOrThrowEntityNotFoundException(@NotNull Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(
                        () -> new EntityNotFoundException(
                                "Project with id: " + projectId + " not found"));
    }
}
