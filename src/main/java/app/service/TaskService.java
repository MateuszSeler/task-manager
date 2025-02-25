package app.service;

import app.dto.task.TaskCreateRequestDto;
import app.dto.task.TaskDto;
import app.dto.task.TaskUpdateRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public interface TaskService {
    TaskDto createTask(@NotNull Long projectId,
                       @Valid TaskCreateRequestDto taskCreateRequestDto);

    Set<TaskDto> getTasksFromProject(@NotNull Long projectId);

    TaskDto getTaskById(@NotNull Long taskId);

    TaskDto updateTaskById(@NotNull Long taskId, @Valid TaskUpdateRequestDto requestDto);

    void deleteById(@Valid Long taskId);
}
