package app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import app.dto.project.ProjectCreateRequestDto;
import app.exception.EntityNotFoundException;
import app.mapper.ProjectMapper;
import app.repository.AttachmentRepository;
import app.repository.CommentRepository;
import app.repository.ProjectRepository;
import app.repository.TaskRepository;
import app.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectMapper projectMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private AttachmentRepository attachmentRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    void createProject_nonExistingUser_EntityNotFound() {
        Long userId = 31L;

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> projectService.createProject(
                                userId, new ProjectCreateRequestDto()));

        assertEquals("User with id: " + userId + " not found",
                exception.getMessage());
    }

    @Test
    void getUsersProjects_nonExistingUser_EntityNotFound() {
        Long userId = 31L;

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> projectService.getUsersProjects(userId));

        assertEquals("User with id: " + userId + " not found",
                exception.getMessage());
    }

    @Test
    void getProjectById_nonExistingUser_nonExistingProject_EntityNotFound() {
        Long projectId = 31L;

        Mockito.when(projectRepository.findById(projectId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> projectService.getProjectById(projectId));

        assertEquals("Project with id: " + projectId + " not found",
                exception.getMessage());
    }

    @Test
    void updateProjectById_nonExistingProject_EntityNotFound() {
        Long projectId = 31L;

        Mockito.when(projectRepository.findById(projectId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> projectService.updateProjectById(
                                projectId, new ProjectCreateRequestDto()));

        assertEquals("Project with id: " + projectId + " not found",
                exception.getMessage());
    }

    @Test
    void deleteById_nonExistingProject_EntityNotFound() {
        Long projectId = 31L;

        Mockito.when(projectRepository.findById(projectId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> projectService.deleteById(projectId));

        assertEquals("Project with id: " + projectId + " not found",
                exception.getMessage());
    }
}
