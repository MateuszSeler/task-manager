package app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import app.dto.task.TaskCreateRequestDto;
import app.exception.DataProcessingException;
import app.exception.EntityNotFoundException;
import app.mapper.TaskMapper;
import app.model.Label;
import app.model.Task;
import app.model.User;
import app.repository.AttachmentRepository;
import app.repository.CommentRepository;
import app.repository.LabelRepository;
import app.repository.ProjectRepository;
import app.repository.TaskRepository;
import app.repository.UserRepository;
import app.service.notification.ChangeManager;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LabelRepository labelRepository;
    @Mock
    private ChangeManager changeManager;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private AttachmentRepository attachmentRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void createTask_nonExistingUser_EntityNotFound() {
        Long projectId = 31L;
        Long userId = 31L;
        TaskCreateRequestDto taskCreateRequestDto
                = new TaskCreateRequestDto().setAssigneeId(userId);

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> taskService.createTask(
                                projectId, taskCreateRequestDto));

        assertEquals("User with id: " + userId + " not found",
                exception.getMessage());
    }

    @Test
    void createTask_nonExistingProject_EntityNotFound() {
        Long projectId = 31L;
        Long userId = 31L;
        TaskCreateRequestDto taskCreateRequestDto
                = new TaskCreateRequestDto()
                .setAssigneeId(userId)
                .setProjectId(projectId);

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(new User()));

        Mockito.when(projectRepository.findById(projectId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> taskService.createTask(
                                projectId, taskCreateRequestDto));

        assertEquals("Project with id: " + projectId + " not found",
                exception.getMessage());
    }

    @Test
    void getTasksFromProject_nonExistingProject_EntityNotFound() {
        Long projectId = 31L;

        Mockito.when(projectRepository.findById(projectId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> taskService.getTasksFromProject(projectId));

        assertEquals("Project with id: " + projectId + " not found",
                exception.getMessage());
    }

    @Test
    void getTaskById_nonExistingTask_EntityNotFound() {
        Long taskId = 31L;

        Mockito.when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> taskService.getTaskById(taskId));

        assertEquals("Task with id: " + taskId + " not found",
                exception.getMessage());
    }

    @Test
    void deleteById_nonExistingTask_EntityNotFound() {
        Long taskId = 31L;

        Mockito.when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> taskService.deleteById(taskId));

        assertEquals("Task with id: " + taskId + " not found",
                exception.getMessage());
    }

    @Test
    void addLabelToTask_nonExistingTask_EntityNotFound() {
        Long taskId = 31L;
        Long labelId = 31L;

        Mockito.when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> taskService.addLabelToTask(taskId, labelId));

        assertEquals("Task with id: " + taskId + " not found",
                exception.getMessage());
    }

    @Test
    void addLabelToTask_nonExistingLabel_EntityNotFound() {
        Long taskId = 31L;
        Long labelId = 31L;

        Mockito.when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(new Task()));

        Mockito.when(labelRepository.findById(labelId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> taskService.addLabelToTask(taskId, labelId));

        assertEquals("Label with id: " + labelId + " not found",
                exception.getMessage());
    }

    @Test
    void addLabelToTask_labelAlreadyAttachedToTheTask_DataProcessingException() {
        Long taskId = 31L;
        Long labelId = 31L;
        Label label = new Label();
        Task task = new Task();
        task.getLabels().add(label);

        Mockito.when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        Mockito.when(labelRepository.findById(labelId))
                .thenReturn(Optional.of(label));

        DataProcessingException exception =
                assertThrows(DataProcessingException.class,
                        () -> taskService.addLabelToTask(taskId, labelId));

        assertEquals("Label with id: " + labelId + " is already attached to the task",
                exception.getMessage());
    }

    @Test
    void removeLabelFromTask_nonExistingTask_EntityNotFound() {
        Long taskId = 31L;
        Long labelId = 31L;

        Mockito.when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> taskService.removeLabelFromTask(taskId, labelId));

        assertEquals("Task with id: " + taskId + " not found",
                exception.getMessage());
    }

    @Test
    void removeLabelFromTask_nonExistingLabel_EntityNotFound() {
        Long taskId = 31L;
        Long labelId = 31L;

        Mockito.when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(new Task()));

        Mockito.when(labelRepository.findById(labelId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> taskService.removeLabelFromTask(taskId, labelId));

        assertEquals("Label with id: " + labelId + " not found",
                exception.getMessage());
    }

    @Test
    void removeLabelFromTask_labelNotAttachedToTheTask_DataProcessingException() {
        Long taskId = 31L;
        Long labelId = 31L;

        Mockito.when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(new Task()));

        Mockito.when(labelRepository.findById(labelId))
                .thenReturn(Optional.of(new Label()));

        DataProcessingException exception =
                assertThrows(DataProcessingException.class,
                        () -> taskService.removeLabelFromTask(taskId, labelId));

        assertEquals("There is no label with id: " + labelId + " attached to the task",
                exception.getMessage());
    }
}
