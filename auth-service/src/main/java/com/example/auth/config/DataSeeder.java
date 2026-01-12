package com.example.auth.config;

import com.example.auth.domain.User;
import com.example.auth.repo.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;
import java.util.UUID;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seedUsers(UserRepo repo) {
        return args -> {
            BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
            repo.findByEmail("admin@demo.com").orElseGet(() -> repo.save(new User(
                    UUID.randomUUID(), "admin@demo.com", enc.encode("Admin@123"), Set.of("ADMIN")
            )));
            repo.findByEmail("user@demo.com").orElseGet(() -> repo.save(new User(
                    UUID.randomUUID(), "user@demo.com", enc.encode("User@123"), Set.of("USER")
            )));
        };
    }
}
