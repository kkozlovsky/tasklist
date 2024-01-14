package ru.kerporation.tasklist.repository.mappers;

import lombok.SneakyThrows;
import ru.kerporation.tasklist.domain.task.Task;
import ru.kerporation.tasklist.domain.user.Role;
import ru.kerporation.tasklist.domain.user.User;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserRowMapper {

    @SneakyThrows
    public static User mapRow(final ResultSet resultSet) {
        final Set<Role> roles = new HashSet<>();
        while (resultSet.next()) {
            roles.add(Role.valueOf(resultSet.getString("user_role_role")));
        }
        // возвращаемся в начало
        resultSet.beforeFirst();
        final List<Task> tasks = TaskRowMapper.mapRows(resultSet);

        resultSet.beforeFirst();
        if (resultSet.next()) {
            final User user = new User();
            user.setId(resultSet.getLong("user_id"));
            user.setName(resultSet.getString("user_name"));
            user.setUsername(resultSet.getString("user_username"));
            user.setPassword(resultSet.getString("user_password"));
            user.setRoles(roles);
            user.setTasks(tasks);
            return user;
        }
        return null;
    }
}
