package ru.kerporation.tasklist.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kerporation.tasklist.domain.exception.ResourceNotFoundException;
import ru.kerporation.tasklist.domain.task.Status;
import ru.kerporation.tasklist.domain.task.Task;
import ru.kerporation.tasklist.repository.TaskRepository;
import ru.kerporation.tasklist.service.TaskService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Transactional(readOnly = true)
    public Task getById(final Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllByUserId(final Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public Task create(final Task task, final Long userId) {
        task.setStatus(Status.TODO);
        taskRepository.create(task);
        taskRepository.assignToUserById(task.getId(), userId);
        return task;
    }

    @Override
    @Transactional
    public Task update(final Task task) {
        if (Objects.isNull(task.getStatus())) {
            task.setStatus(Status.TODO);
        }
        taskRepository.update(task);
        return task;
    }

    @Override
    @Transactional
    public void delete(final Long id) {
        taskRepository.delete(id);
    }
}
