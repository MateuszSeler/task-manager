package app.mapper;

import app.config.MapperConfig;
import app.dto.task.TaskCreateRequestDto;
import app.dto.task.TaskDto;
import app.exception.DataProcessingException;
import app.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface TaskMapper {
    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "priority", source = "priority",
            qualifiedByName = "changePriorityIntoString")
    @Mapping(target = "status", source = "status",
            qualifiedByName = "changeStatusIntoString")
    TaskDto toDto(Task task);

    @Mapping(target = "project", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "priority", source = "priority",
            qualifiedByName = "getPriorityFromString")
    Task toModel(TaskCreateRequestDto requestDto);

    @Named("getPriorityFromString")
    default Task.Priority getPriorityFromString(String stringPriority) {
        try {
            return Task.Priority.valueOf(stringPriority);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new DataProcessingException(
                    "Invalid data format of task priority value: " + stringPriority);
        }
    }

    @Named("changePriorityIntoString")
    default String changePriorityIntoString(Task.Priority priority) {
        return priority.toString();
    }

    @Named("changeStatusIntoString")
    default String changeStatusIntoString(Task.Status status) {
        return status.toString();
    }
}
