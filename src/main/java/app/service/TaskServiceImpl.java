package app.service;

import app.dto.task.TaskCreateRequestDto;
import app.dto.task.TaskDto;
import app.dto.task.TaskUpdateRequestDto;
import app.exception.DataProcessingException;
import app.exception.EntityNotFoundException;
import app.mapper.TaskMapper;
import app.model.Label;
import app.model.Project;
import app.model.Task;
import app.model.User;
import app.repository.LabelRepository;
import app.repository.ProjectRepository;
import app.repository.TaskRepository;
import app.repository.UserRepository;
import app.service.notification.ChangeManager;
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
    private final LabelRepository labelRepository;
    private final ChangeManager changeManager;

    @Override
    public TaskDto createTask(Long projectId, TaskCreateRequestDto requestDto) {
        Task task = taskRepository.save(
                toEntity(requestDto).setStatus(Task.Status.NOT_STARTED));

        changeManager.notedTaskCreated(projectId, task.getId());

        return taskMapper.toDto(task);
    }

    @Override
    public Set<TaskDto> getTasksFromProject(@NotNull Long projectId) {
        getProjectByIdOrThrowEntityNotFoundException(projectId);
        return taskRepository.getTasksFromProjectWithNoUserNoProjectNoLabels(projectId)
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

        taskRepository.save(updatedTask);

        changeManager.notedTaskEdited(updatedTask.getProject().getId(), taskId);

        return taskMapper.toDto(updatedTask);
    }

    @Override
    public void deleteById(Long taskId) {
        Task task = getTaskByIdOrThrowEntityNotFoundException(taskId);
        taskRepository.deleteById(taskId);

        changeManager.notedTaskDeleted(task.getProject().getId(), taskId);
    }

    @Override
    public TaskDto addLabelToTask(Long taskId, Long labelId) {
        Task task = getTaskByIdOrThrowEntityNotFoundException(taskId);
        Label label = getLabelByIdOrThrowEntityNotFoundException(labelId);

        if (task.getLabels().contains(label)) {
            throw new DataProcessingException(
                    "Label with id: " + labelId + " is already attached to the task");
        }

        task.getLabels().add(label);
        taskRepository.save(task);

        changeManager.notedLabelWasAdded(
                task.getProject().getId(), task.getName(), label.getName());

        return taskMapper.toDto(task);
    }

    @Override
    public void removeLabelFromTask(Long taskId, Long labelId) {
        Task task = getTaskByIdOrThrowEntityNotFoundException(taskId);
        Label label = getLabelByIdOrThrowEntityNotFoundException(labelId);

        if (!task.getLabels().contains(label)) {
            throw new DataProcessingException(
                    "There is no label with id: " + labelId + " attached to the task");
        }

        task.getLabels().remove(label);

        changeManager.notedLabelWasRemoved(
                task.getProject().getId(), task.getName(), label.getName());
    }

    private Task toEntity(TaskCreateRequestDto requestDto) {
        User assignee = getUserByIdOrThrowEntityNotFoundException(requestDto.getAssigneeId());
        Project project = getProjectByIdOrThrowEntityNotFoundException(requestDto.getProjectId());

        Task task = taskMapper.toModel(requestDto)
                .setAssignee(assignee)
                .setProject(project);

        if (requestDto.getLabelsIds() != null && !requestDto.getLabelsIds().isEmpty()) {
            Set<Label> labels = requestDto.getLabelsIds()
                    .stream()
                    .map(this::getLabelByIdOrThrowEntityNotFoundException)
                    .collect(Collectors.toSet());

            task.setLabels(labels);
        }

        return task;
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
                () -> new EntityNotFoundException("Task with id: " + taskId + " not found"));
    }

    private Label getLabelByIdOrThrowEntityNotFoundException(@NotNull Long labelId) {
        return labelRepository.findById(labelId).orElseThrow(
                () -> new EntityNotFoundException("Label with id: " + labelId + " not found"));
    }
}
