package app.controller;

import app.dto.label.LabelCreateRequestDto;
import app.dto.label.LabelDto;
import app.dto.label.LabelUpdateRequestDto;
import app.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
@RequestMapping("/labels")
public class LabelController {
    private final LabelService labelService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "creating label",
            description = "creating new label")
    public LabelDto createLabel(
            @RequestBody @Valid LabelCreateRequestDto requestDto) {
        return labelService.createLabel(requestDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    @Operation(summary = "getting labels",
            description = "getting all labels")
    public Set<LabelDto> getLabels() {
        return labelService.getLabels();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{labelId}")
    @Operation(summary = "getting labels",
            description = "getting labels from the project")
    public LabelDto getLabelById(
            @PathVariable @NotNull Long labelId) {
        return labelService.getLabelById(labelId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{labelId}")
    @Operation(summary = "updating label",
            description = "updating label by id")
    public LabelDto updateLabel(
            @PathVariable @NotNull Long labelId,
            @RequestBody @Valid LabelUpdateRequestDto requestDto) {
        return labelService.updateLabel(labelId, requestDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{labelId}")
    @Operation(summary = "deleting label",
            description = "deleting label by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteLabelById(
            @PathVariable @NotNull Long labelId) {
        labelService.deleteLabelById(labelId);
    }
}
