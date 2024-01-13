package ru.kerporation.tasklist.repository.mappers;

import lombok.SneakyThrows;
import ru.kerporation.tasklist.domain.task.Status;
import ru.kerporation.tasklist.domain.task.Task;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class TaskRowMapper {

    @SneakyThrows
    public static Task mapRow(final ResultSet resultSet) {
        if (resultSet.next()) {
            final Task task = new Task();
            task.setId(resultSet.getLong("task_id"));
            task.setTitle(resultSet.getString("task_title"));
            task.setDescription(resultSet.getString("task_description"));
            if (nonNull(resultSet.getTimestamp("task_expiration_date"))) {
                task.setExpirationDate(resultSet.getTimestamp("task_expiration_date").toLocalDateTime());
            }
            task.setStatus(Status.valueOf(resultSet.getString("task_status")));
            return task;
        }
        return null;
    }

    @SneakyThrows
    public static List<Task> mapRows(final ResultSet resultSet) {
        final List<Task> tasks = new ArrayList<>();
        while (resultSet.next()) {
            final Task task = new Task();
            task.setId(resultSet.getLong("task_id"));
            if (!resultSet.wasNull()) {
                task.setTitle(resultSet.getString("task_title"));
                task.setDescription(resultSet.getString("task_description"));
                if (nonNull(resultSet.getTimestamp("task_expiration_date"))) {
                    task.setExpirationDate(resultSet.getTimestamp("task_expiration_date").toLocalDateTime());
                }
                task.setStatus(Status.valueOf(resultSet.getString("task_status")));
                tasks.add(task);
            }

        }
        return tasks;
    }
}
