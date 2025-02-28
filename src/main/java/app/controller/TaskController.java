package app.controller;

import app.dto.task.TaskCreateRequestDto;
import app.dto.task.TaskDto;
import app.dto.task.TaskUpdateRequestDto;
import app.service.TaskService;
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

@Tag(name = "tasks manger",
        description = "Endpoints for managing tasks in projects")
@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class TaskController {
    private final TaskService taskService;

    @PreAuthorize("hasRole('ROLE_USER') "
            + "and @memberService.isUserManagingTheProject(#projectId, authentication.name)")
    @PostMapping("/{projectId}/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "creating task",
            description = "adding new task to the project")
    public TaskDto createTask(
            @PathVariable @Valid Long projectId,
            @RequestBody @Valid TaskCreateRequestDto taskCreateRequestDto) {
        return taskService.createTask(projectId, taskCreateRequestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER') "
            + "and @memberService.whetherUserIsMember(#projectId, authentication.name)")
    @GetMapping("/{projectId}/tasks")
    @Operation(summary = "getting tasks",
            description = "getting tasks from the project")
    public Set<TaskDto> getTasksFromProject(@PathVariable @Valid Long projectId) {
        return taskService.getTasksFromProject(projectId);
    }

    @PreAuthorize("hasRole('ROLE_USER') "
            + "and @memberService.whetherUserIsMember(#projectId, authentication.name)")
    @GetMapping("/{projectId}/tasks/{taskId}")
    @Operation(summary = "getting task",
            description = "getting task by id")
    public TaskDto getTaskById(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long taskId) {
        return taskService.getTaskById(taskId);
    }

    @PreAuthorize("hasRole('ROLE_USER') "
            + "and @memberService.isUserManagingTheProject(#projectId, authentication.name)")
    @PutMapping("/{projectId}/tasks/{taskId}")
    @Operation(summary = "updating tasks",
            description = "updating tasks from the project")
    public TaskDto updateTaskById(
            @PathVariable @Valid Long projectId,
            @PathVariable @NotNull Long taskId,
            @RequestBody @Valid TaskUpdateRequestDto requestDto) {
        return taskService.updateTaskById(taskId, requestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER') "
            + "and @memberService.isUserManagingTheProject(#projectId, authentication.name)")
    @DeleteMapping("/{projectId}/tasks/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "deleting tasks",
            description = "deleting tasks from the project")
    public void deleteTaskById(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long taskId) {
        taskService.deleteById(taskId);
    }

    @PreAuthorize("hasRole('ROLE_USER') "
            + "and @memberService.isUserManagingTheProject(#projectId, authentication.name)")
    @PostMapping("/{projectId}/tasks/{taskId}/labels/{labelId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "adding label",
            description = "adding new label to the task")
    public TaskDto addLabelToTask(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long taskId,
            @PathVariable @Valid Long labelId) {
        return taskService.addLabelToTask(taskId, labelId);
    }

    @PreAuthorize("hasRole('ROLE_USER') "
            + "and @memberService.isUserManagingTheProject(#projectId, authentication.name)")
    @DeleteMapping("/{projectId}/tasks/{taskId}/labels/{labelId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "deleting label",
            description = "deleting label from the task")
    public void removeLabelFromTask(
            @PathVariable @Valid Long projectId,
            @PathVariable @Valid Long taskId,
            @PathVariable @Valid Long labelId) {
        taskService.removeLabelFromTask(taskId, labelId);
    }
}
