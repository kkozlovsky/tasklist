package ru.kerporation.tasklist.service;

import org.springframework.transaction.annotation.Transactional;
import ru.kerporation.tasklist.domain.task.Task;
import ru.kerporation.tasklist.domain.task.TaskImage;

import java.time.Duration;
import java.util.List;

public interface TaskService {
    Task getById(final Long id);

    List<Task> getAllByUserId(final Long userId);

    Task create(final Task task, final Long userId);

    Task update(final Task task);

    void delete(final Long id);

    void uploadImage(final Long id, final TaskImage image);

    @Transactional(readOnly = true)
    List<Task> getAllSoonTasks(Duration duration);
}
