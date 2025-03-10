package app.service;

import app.dto.user.UserResponseDto;
import app.exception.DataProcessingException;
import app.exception.EntityNotFoundException;
import app.mapper.UserMapper;
import app.model.Project;
import app.model.User;
import app.repository.ProjectRepository;
import app.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Component("memberService")
public class MemberServiceImpl implements MemberService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public boolean whetherUserIsMember(@NotNull Long projectId, @NotNull String userEmail) {
        User owner = getUserByEmailOrThrowEntityNotFoundException(userEmail);
        return projectRepository
                .findProjectByUserIdAndProjectId(owner.getId(), projectId)
                .isPresent();
    }

    @Override
    public boolean isUserManagingTheProject(@NotNull Long projectId, @NotNull String userEmail) {
        User owner = getUserByEmailOrThrowEntityNotFoundException(userEmail);
        return projectRepository
                .findManagingProjectByUserIdAndProjectId(owner.getId(), projectId)
                .isPresent();
    }

    @Override
    public Set<UserResponseDto> addUserToProject(Long projectId, Long userId) {
        User user = getUserByIdOrThrowEntityNotFoundException(userId);
        Project project = getProjectByIdOrThrowEntityNotFoundException(projectId);

        if (project.getProjectMembers().contains(user)) {
            throw new DataProcessingException(
                    "User with id: " + userId + " is already a member of this project");
        }

        project.getProjectMembers().add(user);
        projectRepository.save(project);

        return project.getProjectMembers()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<UserResponseDto> deleteUserFromProject(Long projectId, Long userId) {
        User user = getUserByIdOrThrowEntityNotFoundException(userId);
        Project project = getProjectByIdOrThrowEntityNotFoundException(projectId);
        if (!project.getProjectMembers().contains(user)) {
            throw new DataProcessingException(
                    "User with id: " + userId + " is not a member of this project");
        }

        if (project.getProjectMembers().size() <= 1) {
            throw new DataProcessingException(
                    "Project can't have no members");
        }

        if (project.getProjectManagers().contains(user)) {
            removeUserFromTheProjectManagerRole(projectId, userId);
        }

        project.getProjectMembers().remove(user);
        projectRepository.save(project);

        return project.getProjectMembers()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<UserResponseDto> makeUserManagerOfTheProject(Long projectId, Long userId) {
        User user = getUserByIdOrThrowEntityNotFoundException(userId);
        Project project = getProjectByIdOrThrowEntityNotFoundException(projectId);
        if (!project.getProjectMembers().contains(user)) {
            throw new DataProcessingException(
                    "User with id: " + userId + " is not a member of this project");
        }

        if (project.getProjectManagers().contains(user)) {
            throw new DataProcessingException(
                    "User with id: " + userId + " is already a manager of this project");
        }

        project.getProjectManagers().add(user);
        projectRepository.save(project);

        return project.getProjectManagers()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<UserResponseDto> removeUserFromTheProjectManagerRole(Long projectId, Long userId) {
        User user = getUserByIdOrThrowEntityNotFoundException(userId);
        Project project = getProjectByIdOrThrowEntityNotFoundException(projectId);
        if (!project.getProjectManagers().contains(user)) {
            throw new DataProcessingException(
                    "User with id: " + userId + " is not a manager of this project");
        }

        if (project.getProjectManagers().size() <= 1) {
            throw new DataProcessingException(
                    "Project can't have no managers");
        }

        project.getProjectManagers().remove(user);
        projectRepository.save(project);

        return project.getProjectManagers()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toSet());
    }

    private User getUserByIdOrThrowEntityNotFoundException(@NotNull Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + userId + " not found"));
    }

    private User getUserByEmailOrThrowEntityNotFoundException(@NotNull String userEmail) {
        return userRepository.findByEmail(userEmail).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + userEmail + " not found"));
    }

    private Project getProjectByIdOrThrowEntityNotFoundException(@NotNull Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Project with id: " + projectId + " not found"));
    }
}
