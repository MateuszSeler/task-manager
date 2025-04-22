package app.model.task;

import app.model.Competence;
import app.model.Label;
import app.model.Project;
import app.model.user.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Task {
    private Long id;
    @NotNull
    private String name;
    @Size(max = 1000)
    private String description;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDate dueDate;
    @NotNull
    private Project project;
    private User assignee;
    private Set<Label> labels = new HashSet<>();
    private Set<Task> nextTasks = new HashSet<>();
    private Set<Competence> competencesRequired = new HashSet<>();

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH;
    }

    public enum Status {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED;
    }
}
