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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public CommentDto createComment(
            @Valid CommentCreateRequestDto requestDto) {
        Comment comment = toEntity(requestDto).setTimestamp(LocalDateTime.now());
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public Set<CommentDto> getCommentsFromTask(@NotNull Long taskId) {
        return commentRepository.getCommentsFromTaskWithNoTaskNoUser(taskId)
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toSet());
    }

    private Comment toEntity(@Valid CommentCreateRequestDto requestDto) {
        User owner = getUserByIdOrThrowEntityNotFoundException(requestDto.getUserId());
        Task task = getTaskByIdOrThrowEntityNotFoundException(requestDto.getTaskId());

        return commentMapper.toModel(requestDto)
                .setUser(owner)
                .setTask(task);
    }

    private User getUserByIdOrThrowEntityNotFoundException(@NotNull Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + userId + " not found"));
    }

    private Task getTaskByIdOrThrowEntityNotFoundException(@NotNull Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException("Task with id: " + taskId + " not found"));
    }
}
