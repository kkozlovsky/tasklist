package ru.kerporation.tasklist.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.ResourceAccessException;
import ru.kerporation.tasklist.domain.user.Role;
import ru.kerporation.tasklist.domain.user.User;
import ru.kerporation.tasklist.repository.DataSourceConfig;
import ru.kerporation.tasklist.repository.UserRepository;
import ru.kerporation.tasklist.repository.mappers.UserRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

//@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final DataSourceConfig dataSourceConfig;

    private static final String FIND_BY_UD = """
            SELECT users.id       as user_id,
                   users.name     as user_name,
                   users.username as user_username,
                   users.password as user_password,
                   ur.role as user_role_role,
                   tasks.id as task_id,
                   tasks.title as task_title,
                   tasks.description as task_description,
                   tasks.expiration_date as task_expiration_date,
                   tasks.status as task_status
            FROM tasklist.users users
                     LEFT JOIN tasklist.users_roles ur on users.id = ur.user_id
                     LEFT JOIN tasklist.users_tasks ut on users.id = ut.user_id
                     LEFT JOIN tasklist.tasks tasks on ut.task_id = tasks.id
            WHERE users.id = ?""";

    private static final String FIND_BY_USERNAME = """
            SELECT users.id       as user_id,
                   users.name     as user_name,
                   users.username as user_username,
                   users.password as user_password,
                   ur.role as user_role_role,
                   tasks.id as task_id,
                   tasks.title as task_title,
                   tasks.description as task_description,
                   tasks.expiration_date as task_expiration_date,
                   tasks.status as task_status
            FROM tasklist.users users
                     LEFT JOIN tasklist.users_roles ur on users.id = ur.user_id
                     LEFT JOIN tasklist.users_tasks ut on users.id = ut.user_id
                     LEFT JOIN tasklist.tasks tasks on ut.task_id = tasks.id
            WHERE users.username = ?""";

    private final static String UPDATE = """
            UPDATE tasklist.users
            SET name = ?,
                username = ?,
                password = ?
            WHERE id = ?""";

    private final static String CREATE = """
            INSERT INTO tasklist.users (name, username, password)
            VALUES (?,?,?)""";

    private final static String INSERT_USER_ROLE = """
            INSERT INTO tasklist.users_roles (user_id, role)
            VALUES (?,?)""";

    private final static String IS_TASK_OWNER = """
            SELECT EXISTS (SELECT 1
                       FROM tasklist.users_tasks
                       WHERE user_id = ?
                         AND task_id = ?)
            """;

    private final static String DELETE = """
            DELETE FROM tasklist.users
            WHERE id = ?""";

    @Override
    public Optional<User> findById(final Long id) {
        try {
            final Connection connection = dataSourceConfig.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_UD, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setLong(1, id);
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(resultSet));
            }
        } catch (final SQLException e) {
            throw new ResourceAccessException("Error while executing query FIND_BY_ID");
        }
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        try {
            final Connection connection = dataSourceConfig.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_USERNAME, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, username);
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(resultSet));
            }
        } catch (final SQLException e) {
            throw new ResourceAccessException("Error while executing query FIND_BY_USERNAME");
        }
    }

    @Override
    public void update(final User user) {
        try {
            final Connection connection = dataSourceConfig.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setLong(4, user.getId());
            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new ResourceAccessException("Error while executing query UPDATE");
        }
    }

    @Override
    public void create(final User user) {
        try {
            final Connection connection = dataSourceConfig.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.executeUpdate();
            try (final ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                resultSet.next();
                user.setId(resultSet.getLong(1));
            }
        } catch (final SQLException e) {
            throw new ResourceAccessException("Error while executing query CREATE");
        }
    }

    @Override
    public void insertUserRole(final Long userId,
                               final Role role) {
        try {
            final Connection connection = dataSourceConfig.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_ROLE);
            preparedStatement.setLong(1, userId);
            preparedStatement.setString(2, role.name());
            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new ResourceAccessException("Error while executing query INSERT_USER_ROLE");
        }
    }

    @Override
    public boolean isTaskOwner(final Long userId,
                               final Long taskId) {
        try {
            final Connection connection = dataSourceConfig.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(IS_TASK_OWNER);
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, taskId);
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getBoolean(1);
            }
        } catch (final SQLException e) {
            throw new ResourceAccessException("Error while executing query IS_TASK_OWNER");
        }
    }

    @Override
    public void delete(final Long id) {
        try {
            final Connection connection = dataSourceConfig.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new ResourceAccessException("Error while executing query DELETE");
        }
    }
}
