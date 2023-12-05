package ru.kerporation.tasklist.domain.user;

import lombok.Data;

@Data
public class User {

    private Long id;
    private String name;
    private String username;
    private String password;
    private String passwordConfirmation;
    private Set<Role> roles;
    private Set<Task> tasks;
}
