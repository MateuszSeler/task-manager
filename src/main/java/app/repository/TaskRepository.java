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
    Set<Task> getTasksFromProject(@NotNull Long projectId);
}
