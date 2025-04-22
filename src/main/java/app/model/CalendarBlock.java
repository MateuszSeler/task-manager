package app.model;

import app.model.task.Task;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "calendar_blocks")
public class CalendarBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Task task;
    @NotNull
    private LocalDateTime beginning;
    @NotNull
    private LocalDateTime end;
}
