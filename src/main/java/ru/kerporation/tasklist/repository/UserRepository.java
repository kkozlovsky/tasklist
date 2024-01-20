package ru.kerporation.tasklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kerporation.tasklist.domain.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(final String username);

    @Query(value = """
            SELECT EXISTS (SELECT 1
                                   FROM tasklist.users_tasks
                                   WHERE user_id = :userId
                                     AND task_id = :taskId)
            """, nativeQuery = true)
    boolean isTaskOwner(@Param("userId") final Long userId,
                        @Param("taskId") final Long taskId);

}
