package ru.kerporation.tasklist.web.dto.mappers;


import org.mapstruct.Mapper;
import ru.kerporation.tasklist.domain.task.TaskImage;
import ru.kerporation.tasklist.web.dto.task.TaskImageDto;

@Mapper(componentModel = "spring")
public interface TaskImageMapper extends Mappable<TaskImage, TaskImageDto> {
}