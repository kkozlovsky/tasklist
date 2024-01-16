package ru.kerporation.tasklist.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kerporation.tasklist.domain.user.User;
import ru.kerporation.tasklist.service.AuthService;
import ru.kerporation.tasklist.service.UserService;
import ru.kerporation.tasklist.web.dto.auth.JwtRequest;
import ru.kerporation.tasklist.web.dto.auth.JwtResponse;
import ru.kerporation.tasklist.web.dto.mappers.UserMapper;
import ru.kerporation.tasklist.web.dto.user.UserDto;
import ru.kerporation.tasklist.web.dto.validation.OnCreate;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Controller", description = "Auth API")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody final JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public UserDto register(@Validated(OnCreate.class) @RequestBody final UserDto userDto) {
        final User user = userMapper.toEntity(userDto);
        final User createdUser = userService.create(user);
        return userMapper.toDto(createdUser);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@Validated @RequestBody final String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
