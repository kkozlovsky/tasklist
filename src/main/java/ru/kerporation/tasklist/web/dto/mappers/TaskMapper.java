package ru.kerporation.tasklist.web.dto.mappers;

import org.mapstruct.Mapper;
import ru.kerporation.tasklist.domain.task.Task;
import ru.kerporation.tasklist.web.dto.task.TaskDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper extends Mappable<Task, TaskDto> {
}
