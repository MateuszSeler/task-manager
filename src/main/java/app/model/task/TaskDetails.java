package app.model.task;

import app.model.Label;
import app.model.Project;
import app.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "tasks")
public class TaskDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @Size(max = 1000)
    private String description;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Task.Priority priority;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Task.Status status;
    private LocalDate dueDate;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User assignee;
    @ManyToMany
    @JoinTable(name = "tasks_labels",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id"))
    private Set<Label> labels = new HashSet<>();
}
