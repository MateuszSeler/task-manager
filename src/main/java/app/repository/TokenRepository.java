package app.repository;

import app.model.Attachment;
import app.model.Token;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Attachment> findByApiName(@NonNull String apiName);
}
