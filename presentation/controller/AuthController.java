package com.example.booking.presentation.controller;

import com.example.booking.domain.model.User;
import com.example.booking.infrastructure.entity.UserEntity;
import com.example.booking.infrastructure.service.JwtService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Repository
interface SpringJpaUserRepository extends JpaRepository<UserEntity, Long> {
    java.util.Optional<UserEntity> findByEmail(String email);
}

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SpringJpaUserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserEntity user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Ошибка: Email уже существует!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole("ROLE_CLIENT");
        }
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        UserEntity entity = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!passwordEncoder.matches(request.getPassword(), entity.getPassword())) {
            return ResponseEntity.status(401).body("Ошибка: Неверный пароль!");
        }

        User domainUser = new User(entity.getId(), entity.getEmail(), entity.getFullName(), entity.getRole());
        String token = jwtService.generateToken(domainUser);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", entity.getRole());
        response.put("fullName", entity.getFullName());
        
        return ResponseEntity.ok(response);
    }
}

@Data
class LoginRequest {
    private String email;
    private String password;
}
