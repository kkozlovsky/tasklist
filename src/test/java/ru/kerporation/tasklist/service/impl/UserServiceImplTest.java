package ru.kerporation.tasklist.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kerporation.tasklist.config.TestConfig;
import ru.kerporation.tasklist.domain.exception.ResourceNotFoundException;
import ru.kerporation.tasklist.domain.mail.MailType;
import ru.kerporation.tasklist.domain.user.Role;
import ru.kerporation.tasklist.domain.user.User;
import ru.kerporation.tasklist.repository.UserRepository;

import java.util.Optional;
import java.util.Properties;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private MailServiceImpl mailService;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getById() {
        final Long id = 1L;
        User user = new User();
        user.setId(id);
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
        final User testUser = userService.getById(id);
        Mockito.verify(userRepository).findById(id);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByNotExistingId() {
        final Long id = 1L;
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getById(id));
        Mockito.verify(userRepository).findById(id);
    }

    @Test
    void getByUsername() {
        final String username = "username@gmail.com";
        final User user = new User();
        user.setUsername(username);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        final User testUser = userService.getByUsername(username);
        Mockito.verify(userRepository).findByUsername(username);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByNotExistingUsername() {
        final String username = "username@gmail.com";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getByUsername(username));
        Mockito.verify(userRepository).findByUsername(username);
    }

    @Test
    void update() {
        final Long id = 1L;
        final String password = "password";
        final User user = new User();
        user.setId(id);
        user.setPassword(password);
        Mockito.when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        final User updated = userService.update(user);
        Mockito.verify(passwordEncoder).encode(password);
        Mockito.verify(userRepository).save(user);
        Assertions.assertEquals(user.getUsername(), updated.getUsername());
        Assertions.assertEquals(user.getName(), updated.getName());
    }

    @Test
    void isTaskOwner() {
        final Long userId = 1L;
        final Long taskId = 1L;
        Mockito.when(userRepository.isTaskOwner(userId, taskId)).thenReturn(true);
        boolean isOwner = userService.isTaskOwner(userId, taskId);
        Mockito.verify(userRepository).isTaskOwner(userId, taskId);
        Assertions.assertTrue(isOwner);
    }

    @Test
    void create() {
        final String username = "username@gmail.com";
        final String password = "password";
        final User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        final User testUser = userService.create(user);
        Mockito.verify(userRepository).save(user);
        Mockito.verify(mailService).sendEmail(user, MailType.REGISTRATION, new Properties());
        Assertions.assertEquals(Set.of(Role.ROLE_USER), testUser.getRoles());
        Assertions.assertEquals("encodedPassword", testUser.getPassword());
    }

    @Test
    void createWithExistingUsername() {
        final String username = "username@gmail.com";
        final String password = "password";
        final User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));
        Mockito.when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        Assertions.assertThrows(IllegalStateException.class, () -> userService.create(user));
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    @Test
    void createWithDifferentPasswords() {
        final String username = "username@gmail.com";
        final String password = "password1";
        final String passwordConfirmation = "password2";
        final User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(passwordConfirmation);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalStateException.class, () -> userService.create(user));
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    @Test
    void isTaskOwnerWithFalse() {
        final Long userId = 1L;
        final Long taskId = 1L;
        Mockito.when(userRepository.isTaskOwner(userId, taskId)).thenReturn(false);
        boolean isOwner = userService.isTaskOwner(userId, taskId);
        Mockito.verify(userRepository).isTaskOwner(userId, taskId);
        Assertions.assertFalse(isOwner);
    }

    @Test
    void getTaskAuthor() {
        final Long taskId = 1L;
        final Long userId = 1L;
        final User user = new User();
        user.setId(userId);
        Mockito.when(userRepository.findTaskAuthor(taskId)).thenReturn(Optional.of(user));
        final User author = userService.getTaskAuthor(taskId);
        Mockito.verify(userRepository).findTaskAuthor(taskId);
        Assertions.assertEquals(user, author);
    }

    @Test
    void getNotExistingTaskAuthor() {
        final Long taskId = 1L;
        Mockito.when(userRepository.findTaskAuthor(taskId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getTaskAuthor(taskId));
        Mockito.verify(userRepository).findTaskAuthor(taskId);
    }

    @Test
    void delete() {
        final Long id = 1L;
        userService.delete(id);
        Mockito.verify(userRepository).deleteById(id);
    }

}