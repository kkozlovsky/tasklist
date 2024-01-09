package ru.kerporation.tasklist.repository;

import ru.kerporation.tasklist.domain.task.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Optional<Task> findById(final Long id);

    List<Task> findAllByUserId(final Long userId);

    void assignToUserById(final Long taskId, final Long userId);

    void create(final Task task);

    void update(final Task task);

    void delete(final Long id);
}
