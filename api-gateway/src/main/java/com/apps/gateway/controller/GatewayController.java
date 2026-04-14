package com.apps.gateway.controller;

import com.apps.gateway.filter.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class GatewayController {

    private final WebClient webClient = WebClient.create();
    private final JwtUtil jwtUtil;

    @Value("${auth.service.url}")
    private String authUrl;

    @Value("${quantity.service.url}")
    private String quantityUrl;

    @Value("${history.service.url}")
    private String historyUrl;

    @Value("${admin.service.url}")
    private String adminUrl;

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/auth/login", "/auth/register", "/", "/error"
    );

    public GatewayController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // HOME
    @GetMapping("/")
    public Mono<ResponseEntity<Object>> home() {
        return Mono.just(ResponseEntity.ok(Map.of(
                "status", "UP",
                "message", "API Gateway is running",
                "services", Map.of(
                        "auth", "http://localhost:8081",
                        "quantity", "http://localhost:8083",
                        "history", "http://localhost:8084",
                        "admin", "http://localhost:8082"
                )
        )));
    }

    // ================= AUTH =================

    @PostMapping("/auth/register")
    public Mono<ResponseEntity<Object>> register(@RequestBody Object body) {
        return forward(HttpMethod.POST, authUrl + "/auth/register", body, null);
    }

    @PostMapping("/auth/login")
    public Mono<ResponseEntity<Object>> login(@RequestBody Object body) {
        return forward(HttpMethod.POST, authUrl + "/auth/login", body, null);
    }

    @GetMapping("/auth/validate")
    public Mono<ResponseEntity<Object>> validate(ServerWebExchange exchange) {
        String auth = exchange.getRequest().getHeaders().getFirst("Authorization");
        return forward(HttpMethod.GET, authUrl + "/auth/validate", null, auth);
    }

    // ================= QUANTITY =================

    @PostMapping("/api/v1/quantities/{operation}/{userId}")
    public Mono<ResponseEntity<Object>> quantityOp(
            @PathVariable String operation,
            @PathVariable Long userId,
            @RequestBody Object body,
            ServerWebExchange exchange) {

        if (!isAuthenticated(exchange))
            return unauthorized();

        return forward(HttpMethod.POST,
                quantityUrl + "/api/v1/quantities/" + operation + "/" + userId,
                body,
                getAuth(exchange));
    }

    // ================= HISTORY =================

    @GetMapping("/api/v1/quantities/history")
    public Mono<ResponseEntity<Object>> getHistory(ServerWebExchange exchange) {

        if (!isAuthenticated(exchange))
            return unauthorized();

        String email = extractEmail(exchange);
        String url = historyUrl + "/history" + (email != null ? "?email=" + email : "");

        return forward(HttpMethod.GET, url, null, getAuth(exchange));
    }

    @GetMapping("/api/v1/quantities/history/{operation}")
    public Mono<ResponseEntity<Object>> getHistoryByOp(
            @PathVariable String operation,
            ServerWebExchange exchange) {

        if (!isAuthenticated(exchange))
            return unauthorized();

        String email = extractEmail(exchange);
        String url = historyUrl + "/history/operation/" + operation +
                (email != null ? "?email=" + email : "");

        return forward(HttpMethod.GET, url, null, getAuth(exchange));
    }

    @GetMapping("/api/v1/quantities/count/{operation}")
    public Mono<ResponseEntity<Object>> getCount(@PathVariable String operation,
                                                 ServerWebExchange exchange) {

        if (!isAuthenticated(exchange))
            return unauthorized();

        return forward(HttpMethod.GET,
                historyUrl + "/history/count/" + operation,
                null,
                getAuth(exchange));
    }

    @DeleteMapping("/api/v1/quantities/history/{id}")
    public Mono<ResponseEntity<Object>> deleteById(@PathVariable Long id,
                                                   ServerWebExchange exchange) {

        if (!isAuthenticated(exchange))
            return unauthorized();

        return forward(HttpMethod.DELETE,
                historyUrl + "/history/" + id,
                null,
                getAuth(exchange));
    }

    // ================= ADMIN =================

    @GetMapping("/admin/**")
    public Mono<ResponseEntity<Object>> adminGet(ServerWebExchange exchange) {

        if (!isAuthenticated(exchange))
            return unauthorized();

        String path = exchange.getRequest().getURI().getPath();

        return forward(HttpMethod.GET, adminUrl + path, null, getAuth(exchange));
    }

    @DeleteMapping("/admin/**")
    public Mono<ResponseEntity<Object>> adminDelete(ServerWebExchange exchange) {

        if (!isAuthenticated(exchange))
            return unauthorized();

        String path = exchange.getRequest().getURI().getPath();

        return forward(HttpMethod.DELETE, adminUrl + path, null, getAuth(exchange));
    }

    // ================= HELPERS =================

    private boolean isAuthenticated(ServerWebExchange exchange) {
        String header = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (header == null || !header.startsWith("Bearer ")) return false;
        return jwtUtil.isTokenValid(header.substring(7));
    }

    private String extractEmail(ServerWebExchange exchange) {
        String header = exchange.getRequest().getHeaders().getFirst("Authorization");
        try {
            if (header != null && header.startsWith("Bearer ")) {
                return jwtUtil.extractUsername(header.substring(7));
            }
        } catch (Exception ignored) {}
        return null;
    }

    private String getAuth(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst("Authorization");
    }

    private Mono<ResponseEntity<Object>> unauthorized() {
        return Mono.just(ResponseEntity.status(401)
                .body(Map.of("message", "Unauthorized")));
    }

    private Mono<ResponseEntity<Object>> forward(HttpMethod method,
                                                 String url,
                                                 Object body,
                                                 String authHeader) {

        return webClient
                .method(method)
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION,
                        authHeader != null ? authHeader : "")
                .bodyValue(body == null ? "" : body)
                .retrieve()
                .toEntity(Object.class)
                .map(response -> ResponseEntity.status(response.getStatusCode())
                        .body(response.getBody()))
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.status(503)
                                .body(Map.of("message", "Service unavailable: " + e.getMessage())))
                );
    }
}