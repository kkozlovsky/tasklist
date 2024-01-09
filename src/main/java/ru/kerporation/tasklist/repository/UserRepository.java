package ru.kerporation.tasklist.repository;

import ru.kerporation.tasklist.domain.user.Role;
import ru.kerporation.tasklist.domain.user.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(final Long id);
    Optional<User> findByUsername(final String username);
    void update(final User user);
    void create(final User user);
    void insertUserRole(final Long userId, final Role role);
    boolean isTaskOwner(final Long userId, final Long taskId);
    void delete(final Long id);
}
