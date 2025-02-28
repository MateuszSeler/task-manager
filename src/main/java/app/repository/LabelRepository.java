package app.repository;

import app.model.Label;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LabelRepository extends JpaRepository<Label, Long> {
    @Query(value = "SELECT * "
            + "FROM labels "
            + "WHERE project_id = :projectId",
            nativeQuery = true)
    Set<Label> findLabelsFromTheProject(Long projectId);
}
