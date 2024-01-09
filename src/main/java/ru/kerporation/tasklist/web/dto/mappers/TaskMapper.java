package ru.kerporation.tasklist.web.dto.mappers;

import org.mapstruct.Mapper;
import ru.kerporation.tasklist.domain.task.Task;
import ru.kerporation.tasklist.web.dto.task.TaskDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto toDto(final Task task);

    Task toEntity(final TaskDto dto);

    List<TaskDto> toDto(final List<Task> tasks);
}
