package ru.kerporation.tasklist.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.kerporation.tasklist.domain.user.Role;
import ru.kerporation.tasklist.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JwtEntityFactory {
    public static JwtEntity create(final User user) {
        return new JwtEntity(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getPassword(),
                mapToGrantedAuthorities(new ArrayList<>(user.getRoles())));
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(final List<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
