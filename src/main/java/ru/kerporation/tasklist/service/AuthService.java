package ru.kerporation.tasklist.service;

import ru.kerporation.tasklist.web.dto.auth.JwtRequest;
import ru.kerporation.tasklist.web.dto.auth.JwtResponse;

public interface AuthService {
    JwtResponse login(final JwtRequest loginRequest);

    JwtResponse refresh(final String refreshToken);
}
