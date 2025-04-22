package app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import app.exception.DataProcessingException;
import app.exception.EntityNotFoundException;
import app.model.Project;
import app.model.user.User;
import app.repository.ProjectRepository;
import app.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    void whetherUserIsMember_nonExistingUser_EntityNotFound() {
        Long projectId = 31L;
        String userEmail = "email@gmail.com";

        Mockito.when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> memberService.whetherUserIsMember(projectId, userEmail));

        assertEquals("User with email: " + userEmail + " not found",
                exception.getMessage());
    }

    @Test
    void isUserManagingTheProject_nonExistingUser_EntityNotFound() {
        Long projectId = 31L;
        String userEmail = "email@gmail.com";

        Mockito.when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> memberService.isUserManagingTheProject(projectId, userEmail));

        assertEquals("User with email: " + userEmail + " not found",
                exception.getMessage());
    }

    @Test
    void addUserToProject_nonExistingUser_EntityNotFound() {
        Long projectId = 31L;
        Long userId = 31L;

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> memberService.addUserToProject(projectId, userId));

        assertEquals("User with id: " + userId + " not found",
                exception.getMessage());
    }

    @Test
    void addUserToProject_nonExistingProject_EntityNotFound() {
        Long projectId = 31L;
        Long userId = 31L;

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(new User()));

        Mockito.when(projectRepository.findById(projectId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> memberService.addUserToProject(projectId, userId));

        assertEquals("Project with id: " + projectId + " not found",
                exception.getMessage());
    }

    @Test
    void addUserToProject_userIsAlreadyMember_DataProcessingException() {
        Long projectId = 31L;
        Long userId = 31L;
        User user = new User();
        Project project = new Project();
        project.getProjectMembers().add(user);

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        Mockito.when(projectRepository.findById(projectId))
                .thenReturn(Optional.of(project));

        DataProcessingException exception =
                assertThrows(DataProcessingException.class,
                        () -> memberService.addUserToProject(projectId, userId));

        assertEquals("User with id: " + userId + " is already a member of this project",
                exception.getMessage());
    }

    @Test
    void deleteUserFromProject_nonExistingUser_EntityNotFound() {
        Long projectId = 31L;
        Long userId = 31L;

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> memberService.deleteUserFromProject(projectId, userId));

        assertEquals("User with id: " + userId + " not found",
                exception.getMessage());
    }

    @Test
    void deleteUserFromProject_nonExistingProject_EntityNotFound() {
        Long projectId = 31L;
        Long userId = 31L;

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(new User()));

        Mockito.when(projectRepository.findById(projectId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> memberService.deleteUserFromProject(projectId, userId));

        assertEquals("Project with id: " + projectId + " not found",
                exception.getMessage());
    }

    @Test
    void deleteUserFromProject_userIsNotAMember_DataProcessingException() {
        Long projectId = 31L;
        Long userId = 31L;
        User user = new User();
        Project project = new Project();

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        Mockito.when(projectRepository.findById(projectId))
                .thenReturn(Optional.of(project));

        DataProcessingException exception =
                assertThrows(DataProcessingException.class,
                        () -> memberService.deleteUserFromProject(projectId, userId));

        assertEquals("User with id: " + userId + " is not a member of this project",
                exception.getMessage());
    }

    @Test
    void deleteUserFromProject_userIsTheLastMember_DataProcessingException() {
        Long projectId = 31L;
        Long userId = 31L;
        User user = new User();
        Project project = new Project();
        project.getProjectMembers().add(user);

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        Mockito.when(projectRepository.findById(projectId))
                .thenReturn(Optional.of(project));

        DataProcessingException exception =
                assertThrows(DataProcessingException.class,
                        () -> memberService.deleteUserFromProject(projectId, userId));

        assertEquals("Project can't have no members",
                exception.getMessage());
    }

    @Test
    void makeUserManagerOfTheProject_nonExistingUser_EntityNotFound() {
        Long projectId = 31L;
        Long userId = 31L;

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> memberService.makeUserManagerOfTheProject(projectId, userId));

        assertEquals("User with id: " + userId + " not found",
                exception.getMessage());
    }

    @Test
    void makeUserManagerOfTheProject_nonExistingProject_EntityNotFound() {
        Long projectId = 31L;
        Long userId = 31L;

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(new User()));

        Mockito.when(projectRepository.findById(projectId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> memberService.makeUserManagerOfTheProject(projectId, userId));

        assertEquals("Project with id: " + projectId + " not found",
                exception.getMessage());
    }

    @Test
    void makeUserManagerOfTheProject_userIsNotAMember_DataProcessingException() {
        Long projectId = 31L;
        Long userId = 31L;
        User user = new User();
        Project project = new Project();

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        Mockito.when(projectRepository.findById(projectId))
                .thenReturn(Optional.of(project));

        DataProcessingException exception =
                assertThrows(DataProcessingException.class,
                        () -> memberService.makeUserManagerOfTheProject(projectId, userId));

        assertEquals("User with id: " + userId + " is not a member of this project",
                exception.getMessage());
    }

    @Test
    void makeUserManagerOfTheProject_userIsAlreadyMember_DataProcessingException() {
        Long projectId = 31L;
        Long userId = 31L;
        User user = new User();
        Project project = new Project();
        project.getProjectMembers().add(user);
        project.getProjectManagers().add(user);

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        Mockito.when(projectRepository.findById(projectId))
                .thenReturn(Optional.of(project));

        DataProcessingException exception =
                assertThrows(DataProcessingException.class,
                        () -> memberService.makeUserManagerOfTheProject(projectId, userId));

        assertEquals("User with id: " + userId + " is already a manager of this project",
                exception.getMessage());
    }

    @Test
    void removeUserFromTheProjectManagerRole_nonExistingUser_EntityNotFound() {
        Long projectId = 31L;
        Long userId = 31L;

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> memberService.removeUserFromTheProjectManagerRole(projectId, userId));

        assertEquals("User with id: " + userId + " not found",
                exception.getMessage());
    }

    @Test
    void removeUserFromTheProjectManagerRole_nonExistingProject_EntityNotFound() {
        Long projectId = 31L;
        Long userId = 31L;

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(new User()));

        Mockito.when(projectRepository.findById(projectId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> memberService.removeUserFromTheProjectManagerRole(projectId, userId));

        assertEquals("Project with id: " + projectId + " not found",
                exception.getMessage());
    }

    @Test
    void removeUserFromTheProjectManagerRole_userIsNotAManager_DataProcessingException() {
        Long projectId = 31L;
        Long userId = 31L;
        User user = new User();
        Project project = new Project();

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        Mockito.when(projectRepository.findById(projectId))
                .thenReturn(Optional.of(project));

        DataProcessingException exception =
                assertThrows(DataProcessingException.class,
                        () -> memberService.removeUserFromTheProjectManagerRole(projectId, userId));

        assertEquals("User with id: " + userId + " is not a manager of this project",
                exception.getMessage());
    }

    @Test
    void removeUserFromTheProjectManagerRole_userIsTheLastManager_DataProcessingException() {
        Long projectId = 31L;
        Long userId = 31L;
        User user = new User();
        Project project = new Project();
        project.getProjectMembers().add(user);
        project.getProjectManagers().add(user);

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        Mockito.when(projectRepository.findById(projectId))
                .thenReturn(Optional.of(project));

        DataProcessingException exception =
                assertThrows(DataProcessingException.class,
                        () -> memberService.removeUserFromTheProjectManagerRole(projectId, userId));

        assertEquals("Project can't have no managers",
                exception.getMessage());
    }
}
