package ru.kerporation.tasklist.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kerporation.tasklist.domain.task.Task;
import ru.kerporation.tasklist.service.TaskService;
import ru.kerporation.tasklist.web.dto.mappers.TaskMapper;
import ru.kerporation.tasklist.web.dto.task.TaskDto;
import ru.kerporation.tasklist.web.dto.validation.OnUpdate;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping("/{id}")
    public TaskDto getById(@PathVariable final Long id) {
        final Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @PutMapping
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody final TaskDto taskDto) {
        final Task task = taskMapper.toEntity(taskDto);
        final Task updatedTask = taskService.update(task);
        return taskMapper.toDto(updatedTask);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable final Long id) {
        taskService.delete(id);
    }
}
