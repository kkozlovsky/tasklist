package ru.kerporation.tasklist.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.kerporation.tasklist.domain.exception.AccessDeniedException;
import ru.kerporation.tasklist.domain.user.Role;
import ru.kerporation.tasklist.domain.user.User;
import ru.kerporation.tasklist.service.UserService;
import ru.kerporation.tasklist.service.props.JwtProperties;
import ru.kerporation.tasklist.web.dto.auth.JwtResponse;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(final Long userId,
                                    final String username,
                                    Set<Role> roles) {
        final Claims claims = Jwts.claims().setSubject(username).build();
        claims.put("id", userId);
        claims.put("roles", resolveRoles(roles));
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + jwtProperties.getAccess());
        return Jwts.builder()
                .claims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }


    public String createRefreshToken(final Long userId,
                                     final String username) {
        final Claims claims = Jwts.claims().setSubject(username).build();
        claims.put("id", userId);
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + jwtProperties.getRefresh());
        return Jwts.builder()
                .claims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public JwtResponse refreshUserTokens(final String refreshToken) {
        final JwtResponse jwtResponse = new JwtResponse();
        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException();
        }
        final Long userId = Long.valueOf(getId(refreshToken));
        final User user = userService.getById(userId);
        jwtResponse.setId(user.getId());
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(createAccessToken(user.getId(), user.getUsername(), user.getRoles()));
        jwtResponse.setRefreshToken(createRefreshToken(user.getId(), user.getUsername()));
        return jwtResponse;
    }


    public boolean validateToken(final String token) {
        final Jws<Claims> claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        return !claims.getBody().getExpiration().before(new Date());
    }


    public Authentication getAuthentication(final String token) {
        final String username = getUsername(token);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    private String getId(final String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id", String.class);
    }

    private String getUsername(final String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    private List<String> resolveRoles(final Set<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .toList();
    }

}
