package ru.kerporation.tasklist.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kerporation.tasklist.config.TestConfig;
import ru.kerporation.tasklist.domain.exception.ResourceNotFoundException;
import ru.kerporation.tasklist.domain.user.Role;
import ru.kerporation.tasklist.domain.user.User;
import ru.kerporation.tasklist.repository.TaskRepository;
import ru.kerporation.tasklist.repository.UserRepository;
import ru.kerporation.tasklist.web.dto.auth.JwtRequest;
import ru.kerporation.tasklist.web.dto.auth.JwtResponse;
import ru.kerporation.tasklist.web.security.JwtTokenProvider;

import java.util.Collections;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthServiceImpl authService;

    @Test
    void login() {
        final Long userId = 1L;
        final String username = "username";
        final String password = "password";
        final Set<Role> roles = Collections.emptySet();
        final String accessToken = "accessToken";
        final String refreshToken = "refreshToken";
        final JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);
        final User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setRoles(roles);
        Mockito.when(userService.getByUsername(username)).thenReturn(user);
        Mockito.when(tokenProvider.createAccessToken(userId, username, roles)).thenReturn(accessToken);
        Mockito.when(tokenProvider.createRefreshToken(userId, username)).thenReturn(refreshToken);
        final JwtResponse response = authService.login(request);
        Mockito.verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        Assertions.assertEquals(response.getUsername(), username);
        Assertions.assertEquals(response.getId(), userId);
        Assertions.assertNotNull(response.getAccessToken());
        Assertions.assertNotNull(response.getRefreshToken());
    }

    @Test
    void loginWithIncorrectUsername() {
        final String username = "username";
        final String password = "password";
        final JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);
        final User user = new User();
        user.setUsername(username);
        Mockito.when(userService.getByUsername(username)).thenThrow(ResourceNotFoundException.class);
        Mockito.verifyNoInteractions(tokenProvider);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> authService.login(request));
    }

    @Test
    void refresh() {
        final String refreshToken = "refreshToken";
        final String accessToken = "accessToken";
        final String newRefreshToken = "newRefreshToken";
        final JwtResponse response = new JwtResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(newRefreshToken);
        Mockito.when(tokenProvider.refreshUserTokens(refreshToken)).thenReturn(response);
        JwtResponse testResponse = authService.refresh(refreshToken);
        Mockito.verify(tokenProvider).refreshUserTokens(refreshToken);
        Assertions.assertEquals(testResponse, response);
    }

}