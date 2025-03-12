package app.service;

import app.dto.project.ProjectCreateRequestDto;
import app.dto.project.ProjectDto;
import app.exception.DataProcessingException;
import app.exception.EntityNotFoundException;
import app.mapper.ProjectMapper;
import app.model.Attachment;
import app.model.Comment;
import app.model.Project;
import app.model.Task;
import app.model.User;
import app.repository.AttachmentRepository;
import app.repository.CommentRepository;
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
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final AttachmentRepository attachmentRepository;

    @Override
    @Transactional
    public ProjectDto createProject(
            @NotNull Long userId, @Valid ProjectCreateRequestDto requestDto) {
        Project newProject = projectMapper.toModel(requestDto);
        User owner = getUserByIdOrThrowEntityNotFoundException(userId);

        newProject.setStatus(Project.Status.INITIATED);
        newProject.getProjectManagers().add(owner);
        newProject.getProjectMembers().add(owner);

        if (requestDto.getStartDate().isAfter(requestDto.getEndDate())) {
            throw new DataProcessingException("EndDate should be placed after StartDate");
        }

        return projectMapper.toDto(projectRepository.save(newProject));
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public ProjectDto getProjectById(@NotNull Long projectId) {
        return projectMapper.toDto(getProjectByIdOrThrowEntityNotFoundException(projectId));
    }

    @Override
    @Transactional
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
    @Transactional
    public void deleteById(@NotNull Long projectId) {
        getProjectByIdOrThrowEntityNotFoundException(projectId);

        Set<Task> tasksFromProject = taskRepository
                .getTasksFromProjectWithNoUserNoProjectNoLabels(projectId);
        for (Task task : tasksFromProject) {
            for (Comment comment :
                    commentRepository.getCommentsFromTaskWithNoTaskNoUser(task.getId())) {
                commentRepository.deleteById(comment.getId());
            }

            for (Attachment attachment :
                    attachmentRepository.getAttachmentsFromTask(task.getId())) {
                attachmentRepository.deleteById(attachment.getId());
            }

            taskRepository.deleteById(task.getId());
        }

        projectRepository.deleteById(projectId);
    }

    @Transactional(readOnly = true)
    private User getUserByIdOrThrowEntityNotFoundException(@NotNull Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + userId + " not found"));
    }

    @Transactional(readOnly = true)
    private Project getProjectByIdOrThrowEntityNotFoundException(@NotNull Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(
                        () -> new EntityNotFoundException(
                                "Project with id: " + projectId + " not found"));
    }
}
