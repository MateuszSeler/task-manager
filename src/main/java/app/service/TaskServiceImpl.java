package app.service;

import app.dto.task.TaskCreateRequestDto;
import app.dto.task.TaskDto;
import app.dto.task.TaskUpdateRequestDto;
import app.exception.EntityNotFoundException;
import app.mapper.TaskMapper;
import app.model.Project;
import app.model.Task;
import app.model.User;
import app.repository.ProjectRepository;
import app.repository.TaskRepository;
import app.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public TaskDto createTask(Long projectId, TaskCreateRequestDto requestDto) {
        return taskMapper.toDto(
                taskRepository.save(
                        toEntity(requestDto).setStatus(Task.Status.NOT_STARTED)));
    }

    @Override
    public Set<TaskDto> getTasksFromProject(@NotNull Long projectId) {
        getProjectByIdOrThrowEntityNotFoundException(projectId);
        return taskRepository.getTasksFromProject(projectId)
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public TaskDto getTaskById(Long taskId) {
        return taskMapper.toDto(getTaskByIdOrThrowEntityNotFoundException(taskId));
    }

    @Override
    public TaskDto updateTaskById(@NotNull Long taskId, @Valid TaskUpdateRequestDto requestDto) {
        User assignee = getUserByIdOrThrowEntityNotFoundException(requestDto.getAssigneeId());
        Task updatedTask = getTaskByIdOrThrowEntityNotFoundException(taskId)
                .setName(requestDto.getName())
                .setDescription(requestDto.getDescription())
                .setPriority(Task.Priority.valueOf(requestDto.getPriority()))
                .setStatus(Task.Status.valueOf(requestDto.getStatus()))
                .setDueDate(requestDto.getDueDate())
                .setAssignee(assignee);
        return taskMapper.toDto(taskRepository.save(updatedTask));
    }

    @Override
    public void deleteById(Long taskId) {
        getTaskByIdOrThrowEntityNotFoundException(taskId);
        taskRepository.deleteById(taskId);

    }

    private Task toEntity(TaskCreateRequestDto requestDto) {
        User assignee = getUserByIdOrThrowEntityNotFoundException(requestDto.getAssigneeId());
        Project project = getProjectByIdOrThrowEntityNotFoundException(requestDto.getProjectId());
        return taskMapper.toModel(requestDto).setAssignee(assignee).setProject(project);
    }

    private User getUserByIdOrThrowEntityNotFoundException(@NotNull Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + userId + " not found"));
    }

    private Project getProjectByIdOrThrowEntityNotFoundException(@NotNull Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException("Project with id: " + projectId + " not found"));
    }

    private Task getTaskByIdOrThrowEntityNotFoundException(@NotNull Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException("Project with id: " + taskId + " not found"));
    }
}
