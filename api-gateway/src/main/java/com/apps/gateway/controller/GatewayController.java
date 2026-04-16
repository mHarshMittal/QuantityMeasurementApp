package com.apps.gateway.controller;

import com.apps.gateway.filter.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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

    @Value("${admin.service.url:}")
    private String adminUrl;

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/auth/login", "/auth/register", "/", "/error"
    );

    public GatewayController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/")
    public ResponseEntity<?> home() {
        return ResponseEntity.ok(java.util.Map.of(
            "status", "UP",
            "message", "API Gateway is running",
            "services", java.util.Map.of(
                "auth", authUrl,
                "quantity", quantityUrl,
                "history", historyUrl
            )
        ));
    }

    // ── AUTH routes (public) ──────────────────────────────────────────────────
    @PostMapping("/auth/register")
    public ResponseEntity<Object> register(@RequestBody Object body) {
        return forward(HttpMethod.POST, authUrl + "/auth/register", body, null);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody Object body) {
        return forward(HttpMethod.POST, authUrl + "/auth/login", body, null);
    }

    @GetMapping("/auth/validate")
    public ResponseEntity<Object> validate(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        return forward(HttpMethod.GET, authUrl + "/auth/validate", null, auth);
    }

    // ── QUANTITY routes (protected) ───────────────────────────────────────────
    @PostMapping("/api/v1/quantities/{operation}/{userId}")
    public ResponseEntity<Object> quantityOp(@PathVariable String operation,
                                              @PathVariable Long userId,
                                              @RequestBody Object body,
                                              HttpServletRequest request) {
        if (!isAuthenticated(request))
            return ResponseEntity.status(401).body(java.util.Map.of("message", "Unauthorized"));
        return forward(HttpMethod.POST,
                quantityUrl + "/api/v1/quantities/" + operation + "/" + userId,
                body, request.getHeader("Authorization"));
    }

    // ── HISTORY routes (protected) ────────────────────────────────────────────
    @GetMapping("/api/v1/quantities/history")
    public ResponseEntity<Object> getHistory(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return ResponseEntity.status(401).body(java.util.Map.of("message", "Unauthorized"));
        String email = extractEmail(request);
        String url = historyUrl + "/history" + (email != null ? "?email=" + email : "");
        return forward(HttpMethod.GET, url, null, request.getHeader("Authorization"));
    }

    @GetMapping("/api/v1/quantities/history/{operation}")
    public ResponseEntity<Object> getHistoryByOp(@PathVariable String operation,
                                                   HttpServletRequest request) {
        if (!isAuthenticated(request))
            return ResponseEntity.status(401).body(java.util.Map.of("message", "Unauthorized"));
        String email = extractEmail(request);
        String url = historyUrl + "/history/operation/" + operation
                     + (email != null ? "?email=" + email : "");
        return forward(HttpMethod.GET, url, null, request.getHeader("Authorization"));
    }

    @GetMapping("/api/v1/quantities/count/{operation}")
    public ResponseEntity<Object> getCount(@PathVariable String operation, HttpServletRequest request) {
        if (!isAuthenticated(request))
            return ResponseEntity.status(401).body(java.util.Map.of("message", "Unauthorized"));
        return forward(HttpMethod.GET, historyUrl + "/history/count/" + operation, null,
                       request.getHeader("Authorization"));
    }

    @DeleteMapping("/api/v1/quantities/history/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id, HttpServletRequest request) {
        if (!isAuthenticated(request))
            return ResponseEntity.status(401).body(java.util.Map.of("message", "Unauthorized"));
        return forward(HttpMethod.DELETE, historyUrl + "/history/" + id, null,
                       request.getHeader("Authorization"));
    }

    @DeleteMapping("/api/v1/quantities/history")
    public ResponseEntity<Object> deleteAll(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return ResponseEntity.status(401).body(java.util.Map.of("message", "Unauthorized"));
        String email = extractEmail(request);
        String url = historyUrl + "/history" + (email != null ? "?email=" + email : "");
        return forward(HttpMethod.DELETE, url, null, request.getHeader("Authorization"));
    }

    // ── ADMIN routes (protected, only if adminUrl is configured) ─────────────
    @GetMapping("/admin/**")
    public ResponseEntity<Object> adminGet(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return ResponseEntity.status(401).body(java.util.Map.of("message", "Unauthorized"));
        if (adminUrl == null || adminUrl.isEmpty())
            return ResponseEntity.status(503).body(java.util.Map.of("message", "Admin service not configured"));
        String path = request.getRequestURI();
        return forward(HttpMethod.GET, adminUrl + path, null, request.getHeader("Authorization"));
    }

    @DeleteMapping("/admin/**")
    public ResponseEntity<Object> adminDelete(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return ResponseEntity.status(401).body(java.util.Map.of("message", "Unauthorized"));
        if (adminUrl == null || adminUrl.isEmpty())
            return ResponseEntity.status(503).body(java.util.Map.of("message", "Admin service not configured"));
        String path = request.getRequestURI();
        return forward(HttpMethod.DELETE, adminUrl + path, null, request.getHeader("Authorization"));
    }

    // ── Helpers ────────────────────────────────────────────────────────────────
    private boolean isAuthenticated(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) return false;
        return jwtUtil.isTokenValid(header.substring(7));
    }

    private String extractEmail(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            try { return jwtUtil.extractUsername(header.substring(7)); } catch (Exception ignored) {}
        }
        return null;
    }

    private ResponseEntity<Object> forward(HttpMethod method, String url, Object body, String authHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (authHeader != null) headers.set("Authorization", authHeader);

            HttpEntity<Object> entity = new HttpEntity<>(body, headers);
            ResponseEntity<Object> response = restTemplate.exchange(url, method, entity, Object.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(503).body(java.util.Map.of(
                "message", "Service unavailable: " + e.getMessage()));
        }
    }
}
