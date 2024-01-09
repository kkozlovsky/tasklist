package ru.kerporation.tasklist.service.impl;

import org.springframework.stereotype.Service;
import ru.kerporation.tasklist.domain.task.Task;
import ru.kerporation.tasklist.service.TaskService;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Override
    public Task getById(Long id) {
        return null;
    }

    @Override
    public List<Task> getAllByUserId(Long userId) {
        return null;
    }

    @Override
    public Task create(Task task) {
        return null;
    }

    @Override
    public Task update(Task task) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
