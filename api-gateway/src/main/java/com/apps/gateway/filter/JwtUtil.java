package com.apps.gateway.filter;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;

@Component
public class JwtUtil {
    private final String SECRET = "quantimeasure-secret-key-2024-secure!!";
    private Key getKey() { return Keys.hmacShaKeyFor(SECRET.getBytes()); }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isTokenValid(String token) {
        try { extractUsername(token); return true; } catch (Exception e) { return false; }
    }
}
