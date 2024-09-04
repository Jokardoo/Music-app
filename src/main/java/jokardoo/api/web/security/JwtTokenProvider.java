package jokardoo.api.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jokardoo.api.domain.exceptions.AccessDeniedException;
import jokardoo.api.domain.user.Role;
import jokardoo.api.domain.user.User;
import jokardoo.api.services.UserService;
import jokardoo.api.services.properties.JwtProperties;
import jokardoo.api.web.dto.auth.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Обеспечивает создание, проверку и взаимодействие с токенами
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(Long userId, String username, Set<Role> roles) {
        // Claims - это объект, который будет хранить информацию о пользователе в самом токене
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", userId);
        claims.put("roles", resolveRoles(roles));

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getAccess() * 1000);


//        Instant validity = Instant.now()
//                .plus(jwtProperties.getAccess(), ChronoUnit.HOURS); // настоящее время + время жизни токена

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    // Преобразуем список ролей в String
    private List<String> resolveRoles(Set<Role> roles) {
        return roles.stream().map(Enum::name).collect(Collectors.toList());
    }

    public String createRefreshToken(Long userId, String username) {
        Claims claims = Jwts.claims().setSubject(username);

        claims.put("id", userId);

        Instant validity = Instant.now()
                .plus(jwtProperties.getRefresh(), ChronoUnit.DAYS);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(validity))
                .signWith(key)
                .compact();
    }

    public JwtResponse refreshUserToken(String refreshToken) {
        JwtResponse jwtResponse = new JwtResponse();

        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException("Access denied exception");
        }

        Long userId = Long.valueOf(getId(refreshToken));
        User user = userService.getById(userId);
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setId(userId);
        jwtResponse.setAccessToken(createAccessToken(userId, user.getUsername(), user.getRoles()));
        jwtResponse.setRefreshToken(createRefreshToken(userId, user.getUsername()));

        return jwtResponse;

    }

    public boolean validateToken(String token) {

        Jws<Claims> claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        return !claims.getBody().getExpiration().before(new Date());


    }

    private String getId(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id")
                .toString();

    }

    private String getUsername(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Authentication getAuthentication(String token) {

        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }

    public static void main(String[] args) {
        String key = "c2ZzO25maHZ1am4gcztkZmh3ZWlmdXdlaGY7d2VuZnVpd2V2cGVmd2VwaHRucndl";
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.DpF11XcNtvzLr6ohKL8lt5keZ9Ebm609dhizc9-x3Sc";
        Jws<Claims> claimsJwt = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

        System.out.println(claimsJwt.getBody().get("name").toString());
    }


}
