package app.service;

import app.dto.comment.CommentCreateRequestDto;
import app.dto.comment.CommentDto;
import jakarta.validation.Valid;
import java.util.Set;

public interface CommentService {
    CommentDto createComment(@Valid CommentCreateRequestDto requestDto);

    Set<CommentDto> getCommentsFromTask(@Valid Long taskId);

    void deleteCommentById(@Valid Long commentId);

    CommentDto updateCommentById(@Valid Long commentId, @Valid CommentCreateRequestDto requestDto);
}
