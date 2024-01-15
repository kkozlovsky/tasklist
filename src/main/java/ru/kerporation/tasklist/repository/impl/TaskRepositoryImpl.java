package ru.kerporation.tasklist.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.ResourceAccessException;
import ru.kerporation.tasklist.domain.task.Task;
import ru.kerporation.tasklist.repository.DataSourceConfig;
import ru.kerporation.tasklist.repository.TaskRepository;
import ru.kerporation.tasklist.repository.mappers.TaskRowMapper;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

//@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final DataSourceConfig dataSourceConfig;

    private static final String FIND_BY_ID = """
            SELECT tasks.id              as task_id,
                   tasks.title           as task_title,
                   tasks.description     as task_description,
                   tasks.expiration_date as task_expiration_date,
                   tasks.status          as task_status
            FROM tasklist.tasks tasks
            WHERE tasks.id = ?""";

    private static final String FIND_ALL_BY_USER_ID = """
            SELECT tasks.id              as task_id,
                   tasks.title           as task_title,
                   tasks.description     as task_description,
                   tasks.expiration_date as task_expiration_date,
                   tasks.status          as task_status
            FROM tasklist.tasks tasks
                   INNER JOIN tasklist.users_tasks ut on tasks.id = ut.task_id
            WHERE ut.user_id = ?""";

    private static final String ASSIGN_TO_USER_BY_ID = """
            INSERT INTO tasklist.users_tasks (user_id, task_id)
            VALUES (?,?)""";

    private static final String DELETE_BY_ID = """
            DELETE FROM tasklist.tasks
            WHERE id = ?""";

    private static final String CREATE = """
            INSERT INTO tasklist.tasks (title, description, expiration_date, status)
            VALUES (?,?,?,?)""";

    private static final String UPDATE = """
            UPDATE tasklist.tasks
            SET title = ?,
            description = ?,
            expiration_date = ?,
            status = ?
            WHERE id = ?""";


    @Override
    public Optional<Task> findById(final Long id) {
        try {
            final Connection connection = dataSourceConfig.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID);
            preparedStatement.setLong(1, id);
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                return Optional.ofNullable(TaskRowMapper.mapRow(resultSet));
            }
        } catch (final SQLException e) {
            throw new ResourceAccessException("Error while executing query FIND_BY_ID");
        }
    }

    @Override
    public List<Task> findAllByUserId(final Long userId) {
        try {
            final Connection connection = dataSourceConfig.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_USER_ID);
            preparedStatement.setLong(1, userId);
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                return TaskRowMapper.mapRows(resultSet);
            }
        } catch (final SQLException e) {
            throw new ResourceAccessException("Error while executing query FIND_ALL_BY_USER_ID");
        }
    }

    @Override
    public void assignToUserById(final Long taskId,
                                 final Long userId) {
        try {
            final Connection connection = dataSourceConfig.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(ASSIGN_TO_USER_BY_ID);
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, taskId);
            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new ResourceAccessException("Error while executing query ASSIGN_TO_USER_BY_ID");
        }
    }

    @Override
    public void create(final Task task) {
        try {
            final Connection connection = dataSourceConfig.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, task.getTitle());
            if (nonNull(task.getDescription())) {
                preparedStatement.setString(2, task.getDescription());
            } else {
                preparedStatement.setNull(2, Types.VARCHAR);
            }
            if (nonNull(task.getExpirationDate())) {
                preparedStatement.setTimestamp(3, Timestamp.valueOf(task.getExpirationDate()));
            } else {
                preparedStatement.setNull(3, Types.TIMESTAMP);
            }
            preparedStatement.setString(4, task.getStatus().name());
            preparedStatement.executeUpdate();
            // возвращаем id созданной таски
            try (final ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                resultSet.next();
                task.setId(resultSet.getLong(1));
            }
        } catch (final SQLException e) {
            throw new ResourceAccessException("Error while executing query CREATE");
        }
    }

    @Override
    public void update(final Task task) {
        try {
            final Connection connection = dataSourceConfig.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, task.getTitle());
            if (nonNull(task.getDescription())) {
                preparedStatement.setString(2, task.getDescription());
            } else {
                preparedStatement.setNull(2, Types.VARCHAR);
            }
            if (nonNull(task.getExpirationDate())) {
                preparedStatement.setTimestamp(3, Timestamp.valueOf(task.getExpirationDate()));
            } else {
                preparedStatement.setNull(3, Types.TIMESTAMP);
            }
            preparedStatement.setString(4, task.getStatus().name());
            preparedStatement.setLong(5, task.getId());
            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new ResourceAccessException("Error while executing query UPDATE");
        }
    }

    @Override
    public void delete(final Long id) {
        try {
            final Connection connection = dataSourceConfig.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new ResourceAccessException("Error while executing query DELETE_BY_ID");
        }
    }
}
