package com.example.auth.service;

import com.example.auth.api.dto.AuthDtos;
import com.example.auth.domain.User;
import com.example.auth.repo.UserRepo;
import com.example.common.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepo repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;
    private final long ttlMillis;

    public AuthService(UserRepo repo,
                       @Value("${app.jwt.secret}") String secret,
                       @Value("${app.jwt.ttlMillis}") long ttlMillis) {
        this.repo = repo;
        this.jwtUtil = new JwtUtil(secret);
        this.ttlMillis = ttlMillis;
    }

    @Transactional
    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest req) {
        repo.findByEmail(req.email().toLowerCase())
                .ifPresent(u -> { throw new IllegalArgumentException("Email already registered"); });

        User u = new User(UUID.randomUUID(), req.email().toLowerCase(), encoder.encode(req.password()), Set.of("USER"));
        repo.save(u);

        String token = jwtUtil.generateToken(u.getId().toString(), u.getEmail(), List.copyOf(u.getRoles()), ttlMillis);
        return new AuthDtos.AuthResponse(token, u.getId().toString(), u.getEmail(), List.copyOf(u.getRoles()));
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest req) {
        User u = repo.findByEmail(req.email().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!encoder.matches(req.password(), u.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(u.getId().toString(), u.getEmail(), List.copyOf(u.getRoles()), ttlMillis);
        return new AuthDtos.AuthResponse(token, u.getId().toString(), u.getEmail(), List.copyOf(u.getRoles()));
    }
}
