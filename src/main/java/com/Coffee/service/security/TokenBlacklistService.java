package com.Coffee.service.security;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    private final ConcurrentHashMap<String, Instant> revokedTokens = new ConcurrentHashMap<>();

    public void revokeToken(String token, Date expiration) {
        String normalizedToken = normalizeToken(token);
        if (normalizedToken == null || expiration == null) {
            return;
        }

        revokedTokens.put(normalizedToken, expiration.toInstant());
        cleanupExpiredTokens();
    }

    public boolean isTokenRevoked(String token) {
        cleanupExpiredTokens();
        String normalizedToken = normalizeToken(token);
        return normalizedToken != null && revokedTokens.containsKey(normalizedToken);
    }

    private void cleanupExpiredTokens() {
        Instant now = Instant.now();
        revokedTokens.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
    }

    private String normalizeToken(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        return token.startsWith("Bearer ") ? token.substring(7).trim() : token.trim();
    }
}

