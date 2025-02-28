package app.controller;

import app.dto.label.LabelCreateRequestDto;
import app.dto.label.LabelDto;
import app.dto.label.LabelUpdateRequestDto;
import app.service.LabelService;
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

@Tag(name = "label manger",
        description = "Endpoints for managing task's labels in the projects")
@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class LabelController {
    private final LabelService labelService;

    @PreAuthorize("hasRole('ROLE_USER') "
            + "and @memberService.whetherUserIsMember(#projectId, authentication.name)")
    @PostMapping("/{projectId}/labels/")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "creating label",
            description = "creating new label in the project")
    public LabelDto createLabel(
            @PathVariable @Valid Long projectId,
            @RequestBody @Valid LabelCreateRequestDto requestDto) {
        return labelService.createLabel(projectId, requestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER') "
            + "and @memberService.isUserManagingTheProject(#projectId, authentication.name)")
    @GetMapping("/{projectId}/labels/")
    @Operation(summary = "getting labels",
            description = "getting labels from the project")
    public Set<LabelDto> getLabelsFromProject(
            @PathVariable @Valid Long projectId) {
        return labelService.getLabelsFromProject(projectId);
    }

    @PreAuthorize("hasRole('ROLE_USER') "
            + "and @memberService.isUserManagingTheProject(#projectId, authentication.name)")
    @PutMapping("/{projectId}/labels/{labelId}")
    @Operation(summary = "updating label",
            description = "updating label by id")
    public LabelDto updateLabel(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long labelId,
            @RequestBody @Valid LabelUpdateRequestDto requestDto) {
        return labelService.updateLabel(labelId, requestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER') "
            + "and @memberService.isUserManagingTheProject(#projectId, authentication.name)")
    @DeleteMapping("/{projectId}/labels/{labelId}")
    @Operation(summary = "getting labels",
            description = "getting labels attached to the task")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteLabelById(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long labelId) {
        labelService.deleteLabelById(labelId);
    }
}
