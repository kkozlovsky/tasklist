package ru.kerporation.tasklist.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ru.kerporation.tasklist.domain.user.Role;
import ru.kerporation.tasklist.domain.user.User;

import java.util.Optional;

@Mapper
public interface UserRepository {

    Optional<User> findById(@Param("id") final Long id);

    Optional<User> findByUsername(@Param("username") final String username);

    void update(@Param("user") final User user);

    void create(@Param("user") final User user);

    void insertUserRole(@Param("userId") final Long userId,
                        @Param("role") final Role role);

    boolean isTaskOwner(@Param("userId") final Long userId,
                        @Param("taskId") final Long taskId);

    void delete(@Param("id") final Long id);
}
