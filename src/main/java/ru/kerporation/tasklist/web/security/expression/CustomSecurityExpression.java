package ru.kerporation.tasklist.web.security.expression;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.kerporation.tasklist.domain.user.Role;
import ru.kerporation.tasklist.service.UserService;
import ru.kerporation.tasklist.web.security.JwtEntity;

@Service("customSecurityExpression")
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final UserService userService;

    public boolean canAccessUser(final Long id) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final JwtEntity user = (JwtEntity) authentication.getPrincipal();


        return user.getId().equals(id) || hasAnyRole(authentication, Role.ROLE_ADMIN);
    }

    public boolean canAccessTask(final Long taskId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final JwtEntity user = (JwtEntity) authentication.getPrincipal();


        return userService.isTaskOwner(user.getId(), taskId);
    }

    private boolean hasAnyRole(final Authentication authentication,
                               final Role... roles) {
        for (Role role : roles) {
            final SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }
}
