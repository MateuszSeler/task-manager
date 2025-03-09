package app.controller;

import app.dto.attachment.AttachmentResponseDto;
import app.service.attachment.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "attachment manger",
        description = "Endpoints for managing task's attachments in the projects")
@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PreAuthorize("@memberService.whetherUserIsMember(#projectId, authentication.name)")
    @PostMapping("/{projectId}/tasks/{taskId}/attachments/")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "upload attachment",
            description = "attaching new file to the task")
    public AttachmentResponseDto add(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long taskId,
            @RequestParam MultipartFile file) {
        return attachmentService.uploadFile(taskId, file);
    }

    @PreAuthorize("@memberService.whetherUserIsMember(#projectId, authentication.name)")
    @GetMapping("/{projectId}/tasks/{taskId}/attachments/{fileId}")
    @Operation(summary = "download attachment",
            description = "downloading attachment attachment by id")
    public byte[] getCommentsFromTask(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long taskId,
            @PathVariable @Valid Long fileId) {
        return attachmentService.downloadFile(fileId);
    }

    @PreAuthorize("@memberService.isUserManagingTheProject(#projectId, authentication.name)")
    @DeleteMapping("/{projectId}/tasks/{taskId}/attachments/{fileId}")
    @Operation(summary = "deleting attachment",
            description = "deleting attachment from the task")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteLabelById(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long taskId,
            @PathVariable @Valid Long fileId) {
        attachmentService.deleteLabelById(fileId);
    }
}
