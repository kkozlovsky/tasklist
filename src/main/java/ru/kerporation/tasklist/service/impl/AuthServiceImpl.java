package ru.kerporation.tasklist.service.impl;

import org.springframework.stereotype.Service;
import ru.kerporation.tasklist.service.AuthService;
import ru.kerporation.tasklist.web.dto.auth.JwtRequest;
import ru.kerporation.tasklist.web.dto.auth.JwtResponse;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        return null;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return null;
    }
}
