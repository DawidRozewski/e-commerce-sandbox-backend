package com.dawidrozewski.sandbox.security.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dawidrozewski.sandbox.security.model.SandboxUserDetails;
import com.dawidrozewski.sandbox.security.model.User;
import com.dawidrozewski.sandbox.security.model.UserRole;
import com.dawidrozewski.sandbox.security.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class LoginController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final long expireTime;
    private final String secret;

    public LoginController(UserRepository userRepository, AuthenticationManager authenticationManager,
                           @Value("${jwt.expireTime}") long expireTime,
                           @Value("${jwt.secret}") String secret) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.expireTime = expireTime;
        this.secret = secret;
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginCredentials loginCredentials) {
        return authenticate(loginCredentials.getUsername(), loginCredentials.getPassword());
    }

    @PostMapping("/register")
    public Token register(@RequestBody @Valid RegisterCredentials registerCredentials) {
        if (!registerCredentials.getPassword().equals(registerCredentials.getRepeatPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (userRepository.existsByUsername(registerCredentials.getUsername())) {
            throw new IllegalArgumentException("User already exist");
        }
        userRepository.save(User.builder()
                .username(registerCredentials.getUsername())
                .password("{bcrypt} " + new BCryptPasswordEncoder().encode(registerCredentials.getPassword()))
                .enabled(true)
                .authorities(List.of(UserRole.ROLE_CUSTOMER))
                .build());
        return authenticate(registerCredentials.getUsername(), registerCredentials.getPassword());
    }

    private Token authenticate(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow();

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getId(), password)
        );

        SandboxUserDetails principal = (SandboxUserDetails) authenticate.getPrincipal();
        String token = JWT.create()
                .withSubject(String.valueOf(principal.getId()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expireTime))
                .sign(Algorithm.HMAC256(secret));
        return new Token(token, principal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .filter(s -> UserRole.ROLE_ADMIN.name().equals(s))
                        .map(s -> true)
                        .findFirst()
                        .orElse(false)
        );
    }

    @Getter
    private static class LoginCredentials {
        private String username;
        private String password;
    }

    @Getter
    private static class RegisterCredentials {
        @Email
        private String username;
        @NotBlank
        private String password;
        @NotBlank
        private String repeatPassword;
    }

    @Getter
    @AllArgsConstructor
    private static class Token {
        private String token;
        private boolean adminAccess;
    }
}
