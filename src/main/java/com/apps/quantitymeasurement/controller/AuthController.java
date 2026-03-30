package com.apps.quantitymeasurement.controller;

import com.apps.quantitymeasurement.entity.User;
import com.apps.quantitymeasurement.repository.UserRepository;
import com.apps.quantitymeasurement.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {

        User existingUser = userRepository.findByEmail(user.getEmail())
                .orElseGet(() -> userRepository.save(user));

        String token = jwtUtil.generateToken(existingUser.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("user", existingUser);
        response.put("token", token);

        return response;
    }
}