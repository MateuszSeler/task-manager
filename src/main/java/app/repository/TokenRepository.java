package app.repository;

import app.model.Token;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("FROM Token token "
            + "WHERE token.apiName = :apiName")
    Optional<Token> findByApiName(@NotBlank String apiName);
}
