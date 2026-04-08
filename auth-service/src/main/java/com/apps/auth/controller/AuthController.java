package com.apps.auth.controller;

import com.apps.auth.entity.User;
import com.apps.auth.repository.UserRepository;
import com.apps.auth.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already registered"));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        saved.setPassword(null);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User existingUser = userRepository.findByEmail(user.getEmail()).orElse(null);

        if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
        }

        String token = jwtUtil.generateToken(existingUser.getEmail());
        existingUser.setPassword(null);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", existingUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("valid", false, "message", "Missing or invalid token"));
        }
        String token = authHeader.substring(7);
        if (jwtUtil.isTokenValid(token)) {
            String email = jwtUtil.extractUsername(token);
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                user.setPassword(null);
                return ResponseEntity.ok(Map.of("valid", true, "email", email, "userId", user.getId(), "user", user));
            }
        }
        return ResponseEntity.status(401).body(Map.of("valid", false, "message", "Invalid token"));
    }
}
