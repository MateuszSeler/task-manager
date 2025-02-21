package app.repository.user;

import app.model.User;
import app.validator.Email;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(@Email String email);

    Optional<User> findById(@NonNull Long id);
}
