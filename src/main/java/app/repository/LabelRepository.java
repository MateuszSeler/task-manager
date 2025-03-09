package app.repository;

import app.model.Label;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LabelRepository extends JpaRepository<Label, Long> {
    @Query("FROM Label label")
    Set<Label> getAll();
}
