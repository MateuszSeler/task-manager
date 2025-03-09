package app.repository;

import app.model.User;
import app.validator.Email;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("FROM User user "
            + "left join fetch user.roles "
            + "WHERE user.email = :email")
    Optional<User> findByEmail(@Email String email);

    @Query("FROM User user "
            + "left join fetch user.roles "
            + "WHERE user.id = :id")
    Optional<User> findById(@NonNull Long id);
}
