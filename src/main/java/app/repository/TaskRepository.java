package app.repository;

import app.model.Task;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "SELECT * "
            + "FROM tasks "
            + "WHERE tasks.project_id = :projectId",
            nativeQuery = true)
    Set<Task> getTasksFromProjectWithNoUserNoProjectNoLabels(@NotNull Long projectId);

    @Query(value = "SELECT * "
            + "FROM tasks "
            + "JOIN tasks_labels "
            + "ON id = tasks_labels.task_id "
            + "WHERE tasks_labels.label_id = :labelId ",
            nativeQuery = true)
    Set<Task> findTasksMarkedByLabel(Long labelId);
}
