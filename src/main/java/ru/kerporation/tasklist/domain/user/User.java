package ru.kerporation.tasklist.domain.user;

import lombok.Data;
import ru.kerporation.tasklist.domain.task.Task;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
public class User implements Serializable {
    private Long id;
    private String name;
    private String username;
    private String password;
    private String passwordConfirmation;
    private Set<Role> roles;
    private List<Task> tasks;
}
