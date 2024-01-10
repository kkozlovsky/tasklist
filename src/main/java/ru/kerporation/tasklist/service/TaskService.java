package ru.kerporation.tasklist.service;

import ru.kerporation.tasklist.domain.task.Task;

import java.util.List;

public interface TaskService {
    Task getById(final Long id);

    List<Task> getAllByUserId(final Long userId);

    Task create(final Task task, final Long userId);

    Task update(final Task task);

    void delete(final Long id);
}
