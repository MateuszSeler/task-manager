package app.controller;

import app.dto.comment.CommentCreateRequestDto;
import app.dto.comment.CommentDto;
import app.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "comment manger",
        description = "Endpoints for managing task's comments in the projects")
@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class CommentController {
    private final CommentService commentService;

    @PreAuthorize("@memberService.whetherUserIsMember(#projectId, authentication.name)")
    @PostMapping("/{projectId}/tasks/{taskId}/comments/")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "creating comment",
            description = "adding new comment to the task")
    public CommentDto createComment(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long taskId,
            @RequestBody @Valid CommentCreateRequestDto requestDto) {
        return commentService.createComment(requestDto);
    }

    @PreAuthorize("@memberService.isUserManagingTheProject(#projectId, authentication.name)")
    @GetMapping("/{projectId}/tasks/{taskId}/comments/")
    @Operation(summary = "getting comment",
            description = "getting comment attached to the task")
    public Set<CommentDto> getCommentsFromTask(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long taskId) {
        return commentService.getCommentsFromTask(taskId);
    }

    @PreAuthorize("@commentService.isUserIsTheAuthor(#commentId, authentication.name)")
    @PutMapping("/{projectId}/tasks/{taskId}/comments/{commentId}")
    @Operation(summary = "updating comment",
            description = "updating comment by id")
    CommentDto updateCommentById(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long taskId,
            @PathVariable @Valid Long commentId,
            @RequestBody @Valid CommentCreateRequestDto requestDto) {
        return commentService.updateCommentById(commentId, requestDto);
    }

    @PreAuthorize("@memberService.isUserManagingTheProject(#projectId, authentication.name) "
            + " or @commentService.isUserIsTheAuthor(#commentId, authentication.name)")
    @DeleteMapping("/{projectId}/tasks/{taskId}/comments/{commentId}")
    @Operation(summary = "deleting comment",
            description = "deleting comment by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteLabelCommentById(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long taskId,
            @PathVariable @Valid Long commentId) {
        commentService.deleteCommentById(commentId);
    }
}
