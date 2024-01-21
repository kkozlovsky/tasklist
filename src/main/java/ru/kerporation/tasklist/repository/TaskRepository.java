package ru.kerporation.tasklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kerporation.tasklist.domain.task.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = """
            SELECT * FROM tasks t
            INNER JOIN users_tasks ut ON t.id = ut.task_id
            WHERE ut.user_id = :userId
            """, nativeQuery = true)
    List<Task> findAllByUserId(@Param("userId") final Long userId);

    @Modifying
    @Query(value = """
            INSERT INTO users_tasks (user_id, task_id)
            VALUES (:userId, :taskId)
            """, nativeQuery = true)
    void assignTask(@Param("userId") final Long userId,
                    @Param("taskId") final Long taskId);

    @Modifying
    @Query(value = """
            INSERT INTO tasks_images (task_id, image)
            VALUES (:id, :fileName)
            """, nativeQuery = true)
    void addImage(@Param("id") final Long id,
                  @Param("fileName") final String fileName);

}
