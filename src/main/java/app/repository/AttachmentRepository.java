package app.repository;

import app.model.Attachment;
import java.util.Optional;
import java.util.Set;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Optional<Attachment> findById(@NonNull Long id);

    Optional<Attachment> findByFileName(@NonNull String fileName);

    @Query(value = "SELECT * "
            + "FROM attachments "
            + "WHERE task_id = :taskId",
            nativeQuery = true)
    Set<Attachment> getAttachmentsFromTask(Long taskId);
}
