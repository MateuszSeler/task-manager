package app.service;

import app.dto.comment.CommentCreateRequestDto;
import app.dto.comment.CommentDto;
import app.exception.EntityNotFoundException;
import app.mapper.CommentMapper;
import app.model.Comment;
import app.model.Task;
import app.model.User;
import app.repository.CommentRepository;
import app.repository.TaskRepository;
import app.repository.UserRepository;
import app.service.notification.ChangeManager;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Component("commentService")
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ChangeManager changeManager;

    @Transactional(readOnly = true)
    public boolean isUserIsTheAuthor(@NotNull Long commentId, @NotNull String userEmail) {
        User author = getUserByEmailOrThrowEntityNotFoundException(userEmail);
        return commentRepository
                .findCommentByUserIdAndCommentId(author.getId(), commentId)
                .isPresent();
    }

    @Override
    @Transactional
    public CommentDto createComment(
            @Valid CommentCreateRequestDto requestDto) {
        Comment comment = commentRepository.save(
                toEntity(requestDto).setTimestamp(LocalDateTime.now()));

        changeManager.notedCommentCreated(comment.getTask().getProject().getId(), comment.getId());

        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<CommentDto> getCommentsFromTask(@NotNull Long taskId) {
        return commentRepository.getCommentsFromTaskWithNoTaskNoUser(taskId)
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void deleteCommentById(@NotNull Long commentId) {
        Comment comment = getCommentByIdOrThrowEntityNotFoundException(commentId);
        commentRepository.deleteById(commentId);

        changeManager.notedCommentDeleted(comment.getTask().getProject().getId(), comment.getId());
    }

    @Override
    @Transactional
    public CommentDto updateCommentById(
            @NotNull Long commentId, @Valid CommentCreateRequestDto requestDto) {
        Comment updatedComment = commentRepository.save(
                getCommentByIdOrThrowEntityNotFoundException(commentId)
                .setText(requestDto.getText()));

        changeManager.notedCommentEdited(updatedComment.getTask().getProject().getId(), commentId);
        return commentMapper.toDto(updatedComment);
    }

    @Transactional(readOnly = true)
    private Comment toEntity(@Valid CommentCreateRequestDto requestDto) {
        User owner = getUserByIdOrThrowEntityNotFoundException(requestDto.getUserId());
        Task task = getTaskByIdOrThrowEntityNotFoundException(requestDto.getTaskId());

        return commentMapper.toModel(requestDto)
                .setUser(owner)
                .setTask(task);
    }

    @Transactional(readOnly = true)
    private Comment getCommentByIdOrThrowEntityNotFoundException(@NotNull Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException("Comment with id: " + commentId + " not found"));
    }

    @Transactional(readOnly = true)
    private User getUserByIdOrThrowEntityNotFoundException(@NotNull Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + userId + " not found"));
    }

    @Transactional(readOnly = true)
    private User getUserByEmailOrThrowEntityNotFoundException(@NotNull String userEmail) {
        return userRepository.findByEmail(userEmail).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + userEmail + " not found"));
    }

    @Transactional(readOnly = true)
    private Task getTaskByIdOrThrowEntityNotFoundException(@NotNull Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException("Task with id: " + taskId + " not found"));
    }
}
