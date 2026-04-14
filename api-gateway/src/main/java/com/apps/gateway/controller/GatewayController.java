package com.apps.gateway.controller;

import com.apps.gateway.filter.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;

@RestController
public class GatewayController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final JwtUtil jwtUtil;

    @Value("${auth.service.url}")
    private String authUrl;

    @Value("${quantity.service.url}")
    private String quantityUrl;

    @Value("${history.service.url}")
    private String historyUrl;

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/auth/login", "/auth/register", "/", "/error"
    );

    public GatewayController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // HOME
    @GetMapping("/")
    public ResponseEntity<?> home() {
        return ResponseEntity.ok(java.util.Map.of(
                "status", "UP",
                "message", "API Gateway running"
        ));
    }

    // AUTH
    @PostMapping("/auth/register")
    public ResponseEntity<Object> register(@RequestBody Object body) {
        return forward(HttpMethod.POST, authUrl + "/auth/register", body, null);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody Object body) {
        return forward(HttpMethod.POST, authUrl + "/auth/login", body, null);
    }

    // QUANTITY
    @PostMapping("/api/v1/quantities/{operation}/{userId}")
    public ResponseEntity<Object> quantityOp(
            @PathVariable String operation,
            @PathVariable Long userId,
            @RequestBody Object body,
            ServerWebExchange exchange) {

        if (!isAuthenticated(exchange))
            return ResponseEntity.status(401).body("Unauthorized");

        return forward(HttpMethod.POST,
                quantityUrl + "/api/v1/quantities/" + operation + "/" + userId,
                body,
                exchange.getRequest().getHeaders().getFirst("Authorization"));
    }

    // HISTORY
    @GetMapping("/api/v1/quantities/history")
    public ResponseEntity<Object> history(ServerWebExchange exchange) {

        if (!isAuthenticated(exchange))
            return ResponseEntity.status(401).body("Unauthorized");

        return forward(HttpMethod.GET,
                historyUrl + "/history",
                null,
                exchange.getRequest().getHeaders().getFirst("Authorization"));
    }

    // AUTH CHECK (FIXED)
    private boolean isAuthenticated(ServerWebExchange exchange) {
        String header = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (header == null || !header.startsWith("Bearer ")) return false;
        return jwtUtil.isTokenValid(header.substring(7));
    }

    // FORWARD
    private ResponseEntity<Object> forward(
            HttpMethod method,
            String url,
            Object body,
            String authHeader) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            if (authHeader != null) {
                headers.set("Authorization", authHeader);
            }

            HttpEntity<Object> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Object> response =
                    restTemplate.exchange(url, method, entity, Object.class);

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());

        } catch (Exception e) {
            return ResponseEntity.status(503).body(
                    java.util.Map.of("message", "Service unavailable", "error", e.getMessage())
            );
        }
    }
}