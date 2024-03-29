package ru.kerporation.tasklist.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "User Controller", description = "User API")
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    @GetMapping("/{id}")
    @QueryMapping(name = "userById")
    @Operation(summary = "Get UserDto by id")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public UserDto getById(@PathVariable @Argument final Long id) {
        final User user = userService.getById(id);
        return userMapper.toDto(user);
    }

    @PutMapping
    @Operation(summary = "Update user")
    @MutationMapping(name = "updateUser")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#userDto.id)")
    public UserDto update(@Validated(OnUpdate.class) @RequestBody @Argument final UserDto userDto) {
        final User user = userMapper.toEntity(userDto);
        final User updatedUser = userService.update(user);
        return userMapper.toDto(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    @MutationMapping(name = "deleteUser")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public void deleteById(@PathVariable @Argument final Long id) {
        userService.delete(id);
    }

    @GetMapping("/{id}/tasks")
    @Operation(summary = "Get all User tasks")
    @QueryMapping(name = "tasksByUserId")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public List<TaskDto> getTasksByUserId(@PathVariable @Argument final Long id) {
        final List<Task> tasks = taskService.getAllByUserId(id);
        return taskMapper.toDto(tasks);
    }

    @PostMapping("/{id}/tasks")
    @Operation(summary = "Add task to user")
    @MutationMapping(name = "createTask")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public TaskDto createTask(@PathVariable @Argument final Long id,
                              @Validated(OnCreate.class) @RequestBody @Argument final TaskDto taskDto) {
        final Task task = taskMapper.toEntity(taskDto);
        final Task createdTask = taskService.create(task, id);
        return taskMapper.toDto(createdTask);
    }

}
