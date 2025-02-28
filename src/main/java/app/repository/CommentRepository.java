package app.repository;

import app.model.Comment;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT * "
            + "FROM comments "
            + "WHERE task_id = :taskId",
            nativeQuery = true)
    Set<Comment> getCommentsFromTaskWithNoTaskNoUser(@NotNull Long taskId);
}
