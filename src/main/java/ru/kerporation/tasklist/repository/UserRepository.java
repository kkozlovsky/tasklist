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

    @Query(value = """
            SELECT u.id as id,
            u.name as name,
            u.username as username,
            u.password as password
            FROM tasklist.users_tasks ut
            JOIN tasklist.users u ON ut.user_id = u.id
            WHERE ut.task_id = :taskId
            """, nativeQuery = true)
    Optional<User> findTaskAuthor(@Param("taskId") Long taskId);

}
