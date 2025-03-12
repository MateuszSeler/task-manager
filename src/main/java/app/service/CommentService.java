package app.service;

import app.dto.comment.CommentCreateRequestDto;
import app.dto.comment.CommentDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public interface CommentService {
    CommentDto createComment(@Valid CommentCreateRequestDto requestDto);

    Set<CommentDto> getCommentsFromTask(@NotNull Long taskId);

    void deleteCommentById(@NotNull Long commentId);

    CommentDto updateCommentById(
            @NotNull Long commentId, @Valid CommentCreateRequestDto requestDto);
}
