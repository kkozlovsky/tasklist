package ru.kerporation.tasklist.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kerporation.tasklist.domain.task.Task;
import ru.kerporation.tasklist.domain.user.User;
import ru.kerporation.tasklist.service.TaskService;
import ru.kerporation.tasklist.service.UserService;
import ru.kerporation.tasklist.web.dto.mappers.TaskMapper;
import ru.kerporation.tasklist.web.dto.mappers.UserMapper;
import ru.kerporation.tasklist.web.dto.task.TaskDto;
import ru.kerporation.tasklist.web.dto.user.UserDto;
import ru.kerporation.tasklist.web.dto.validation.OnCreate;
import ru.kerporation.tasklist.web.dto.validation.OnUpdate;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable final Long id) {
        final User user = userService.getById(id);
        return userMapper.toDto(user);
    }

    @PutMapping
    public UserDto update(@Validated(OnUpdate.class) @RequestBody final UserDto userDto) {
        final User user = userMapper.toEntity(userDto);
        final User updatedUser = userService.update(user);
        return userMapper.toDto(updatedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable final Long id) {
        userService.delete(id);
    }

    @GetMapping("/{id}/tasks")
    public List<TaskDto> getTasksByUserId(@PathVariable final Long id) {
        final List<Task> tasks = taskService.getAllByUserId(id);
        return taskMapper.toDto(tasks);
    }

    @PostMapping("/{id}/tasks")
    public TaskDto createTask(@PathVariable final Long id,
                              @Validated(OnCreate.class) @RequestBody final TaskDto taskDto) {
        final Task task = taskMapper.toEntity(taskDto);
        final Task createdTask = taskService.create(task, id);
        return taskMapper.toDto(createdTask);
    }

}
