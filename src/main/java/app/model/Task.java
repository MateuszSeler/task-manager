package app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User assignee;

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
