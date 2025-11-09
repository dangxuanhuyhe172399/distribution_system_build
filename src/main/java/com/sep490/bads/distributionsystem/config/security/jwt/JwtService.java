package com.sep490.bads.distributionsystem.config.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;                         // >= 32 bytes

    @Value("${jwt.expire-time:86400}")            // giây
    private long expireSeconds;

    private SecretKey key;
    private JwtParser jwtParser;

    @PostConstruct
    void init() {
        String s = normalize(secret);
        byte[] keyBytes = tryDecodeSmart(s);
        if (keyBytes == null) {
            keyBytes = s.getBytes(StandardCharsets.UTF_8);
        }
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret too short (<32 bytes).");
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    /** subject = userId (String) để UserSecurityService parse số. */
    public String generateToken(TokenInfo info) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expireSeconds * 1000);

        return Jwts.builder()
                .setSubject(String.valueOf(info.getUserId()))
                .claim("username", info.getUsername())
                .claim("role", info.getRole())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            jwtParser.parseClaimsJws(stripBearer(token));
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("Invalid JWT: {}", e.getMessage());
            return false;
        }
    }

    /** Lấy subject; fallback uid/username nếu cần. */
    public String extractSubject(String token) {
        Claims c = jwtParser.parseClaimsJws(stripBearer(token)).getBody();
        String sub = c.getSubject();
        if (sub != null && !sub.isBlank()) return sub;

        Object uid = c.get("uid");
        if (uid != null) return String.valueOf(uid);

        Object uname = c.get("username");
        return (uname != null) ? String.valueOf(uname) : null;
    }

    public long getExpireTime() { return expireSeconds; }
    public Key getSigningKey() { return this.key; }

    // ===== helpers =====
    private static String stripBearer(String token) {
        if (token == null) return null;
        return token.startsWith("Bearer ") ? token.substring(7) : token;
    }

    private static String normalize(String s) {
        s = s.trim();
        if ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'"))) {
            s = s.substring(1, s.length() - 1);
        }
        return s.replaceAll("\\s", "");
    }

    private static byte[] tryDecodeSmart(String s) {
        boolean looksUrl = s.indexOf('-') >= 0 || s.indexOf('_') >= 0;
        String candidate = looksUrl ? s.replace('-', '+').replace('_', '/') : s;
        int pad = (4 - (candidate.length() % 4)) % 4;
        candidate = candidate + "=".repeat(pad);
        try {
            return Base64.getDecoder().decode(candidate);
        } catch (IllegalArgumentException e1) {
            try {
                String noPad = s.replace("=", "");
                return Base64.getUrlDecoder().decode(noPad);
            } catch (IllegalArgumentException e2) {
                return null;
            }
        }
    }
}
