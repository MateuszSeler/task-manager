package app.repository;

import app.model.Project;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query(value = "SELECT * "
            + "FROM projects "
            + "JOIN project_members "
            + "ON id = project_members.project_id "
            + "WHERE project_members.user_id = :userId",
            nativeQuery = true)
    Set<Project> findUsersProjects(Long userId);

    @Query(value = "SELECT * "
            + "FROM projects "
            + "JOIN project_members "
            + "ON id = project_members.project_id "
            + "WHERE project_members.user_id = :userId "
            + "AND project_members.project_id = :projectId",
            nativeQuery = true)
    Optional<Project> findProjectByUserIdAndProjectId(Long userId, Long projectId);

    @Query(value = "SELECT * "
            + "FROM projects "
            + "JOIN project_managers "
            + "ON id = project_managers.project_id "
            + "WHERE project_managers.user_id = :userId "
            + "AND project_managers.project_id = :projectId",
            nativeQuery = true)
    Optional<Project> findManagingProjectByUserIdAndProjectId(Long userId, Long projectId);
}
