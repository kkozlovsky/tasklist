package ru.kerporation.tasklist.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ru.kerporation.tasklist.domain.task.Task;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskRepository {

    Optional<Task> findById(@Param("id") final Long id);

    List<Task> findAllByUserId(@Param("userId") final Long userId);

    void assignToUserById(@Param("taskId") final Long taskId,
                          @Param("userId") final Long userId);

    void create(@Param("task") final Task task);

    void update(@Param("task") final Task task);

    void delete(@Param("id") final Long id);
}
