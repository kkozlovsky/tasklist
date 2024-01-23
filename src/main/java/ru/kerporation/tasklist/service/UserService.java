package ru.kerporation.tasklist.service;

import ru.kerporation.tasklist.domain.user.User;

public interface UserService {
    User getById(final Long id);

    User getByUsername(final String username);

    User create(final User user);

    User update(final User user);

    boolean isTaskOwner(final Long userId, final Long taskId);

    void delete(final Long id);

    User getTaskAuthor(Long taskId);
}
