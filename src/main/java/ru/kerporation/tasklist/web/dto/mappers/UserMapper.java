package ru.kerporation.tasklist.web.dto.mappers;

import org.mapstruct.Mapper;
import ru.kerporation.tasklist.domain.user.User;
import ru.kerporation.tasklist.web.dto.user.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(final User user);

    User toEntity(final UserDto dto);
}
