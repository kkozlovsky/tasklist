package ru.kerporation.tasklist.web.dto.user;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String username;
    private String password;
    private String passwordConfirmation;
}
