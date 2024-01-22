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
import ru.kerporation.tasklist.domain.task.TaskImage;
import ru.kerporation.tasklist.service.TaskService;
import ru.kerporation.tasklist.web.dto.mappers.TaskImageMapper;
import ru.kerporation.tasklist.web.dto.mappers.TaskMapper;
import ru.kerporation.tasklist.web.dto.task.TaskDto;
import ru.kerporation.tasklist.web.dto.task.TaskImageDto;
import ru.kerporation.tasklist.web.dto.validation.OnUpdate;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Task Controller", description = "Task API")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final TaskImageMapper taskImageMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get TaskDto by id")
    @QueryMapping(name = "taskById")
    @PreAuthorize("canAccessTask(#id)")
    public TaskDto getById(@PathVariable @Argument final Long id) {
        final Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @PutMapping
    @Operation(summary = "Update task")
    @MutationMapping(name = "updateTask")
    @PreAuthorize("canAccessTask(#taskDto.id)")
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody @Argument final TaskDto taskDto) {
        final Task task = taskMapper.toEntity(taskDto);
        final Task updatedTask = taskService.update(task);
        return taskMapper.toDto(updatedTask);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task")
    @MutationMapping(name = "deleteTask")
    @PreAuthorize("canAccessTask(#id)")
    public void deleteById(@PathVariable @Argument final Long id) {
        taskService.delete(id);
    }

    @PostMapping("/{id}/image")
    @Operation(summary = "Upload image to task")
    @PreAuthorize("canAccessTask(#id)")
    public void uploadImage(@PathVariable final Long id, @Validated @ModelAttribute final TaskImageDto imageDto) {
        final TaskImage image = taskImageMapper.toEntity(imageDto);
        taskService.uploadImage(id, image);
    }
}
