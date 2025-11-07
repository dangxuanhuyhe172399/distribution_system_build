package com.sep490.bads.distributionsystem.config.security.jwt;

//import com.sep490.bads.distributionsystem.utils.TokenInfo;
//import com.sep490.bads.distributionsystem.utils.RemoteCache;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
@Log4j2
public class JwtService {
    @Value(value = "${jwt.secret}")
    private String secret;

    @Value(value = "${jwt.expire-time}")
    private Long expireTime;

    @Value(value = "${jwt.expire-time-refreshToken}")
    private Integer jwtRefreshExpirationMs;

//    @Autowired
//    private CacheKey cacheKey;

    public String generateToken(TokenInfo tokenInfo) {
        Map<String, Object> claims = Map.of(
                "userId", tokenInfo.getUserId(),
                "role", tokenInfo.getRole()
        );
        return createToken(claims, String.valueOf(tokenInfo.getUserId()));
    }

    private String createToken(Map<String, Object> claims, String id) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime * 1000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        String s = secret.trim().replace("\"", "");
        byte[] keyBytes;
        if (s.matches("^[A-Za-z0-9_-]+$")) {               // base64url
            keyBytes = io.jsonwebtoken.io.Decoders.BASE64URL.decode(s);
        } else if (s.matches("^[A-Za-z0-9+/=\\r\\n]+$")) { // base64 chuẩn
            // thêm padding nếu thiếu
            int m = s.length() % 4;
            if (m != 0) s = s + "====".substring(m);
            keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(s);
        } else {                                           // plaintext
            keyBytes = s.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        }
        if (keyBytes.length < 32) throw new IllegalArgumentException("JWT secret too short");
        return Keys.hmacShaKeyFor(keyBytes);
    }


//    public void saveTokenWithExpireTime(String token, String userId) {
//        String hashToken = Helper.md5Token(token);
//
//        String sessionKey = cacheKey.genSessionKey(hashToken);
//        remoteCache.set(sessionKey, hashToken, jwtRefreshExpirationMs);
//
//        String accountSessionKey = cacheKey.genAccountSessionKey(userId);
//
//        String existingToken = remoteCache.get(accountSessionKey);
//        if (existingToken != null) {
//            remoteCache.del(cacheKey.genSessionKey(existingToken));
//        }
//        remoteCache.set(accountSessionKey, hashToken, jwtRefreshExpirationMs);
//    }

//    public String generateRefreshToken(LoginDto loginDTO) throws RuntimeException {
//        UUID uuid = UUID.randomUUID();
//        String hashToken = Helper.md5Token(loginDTO.getUsername() + uuid);
//        String sessionKey = cacheKey.genSessionKey(hashToken);
//        remoteCache.set(sessionKey, JsonParser.toJson(loginDTO), jwtRefreshExpirationMs);
//        return hashToken;
//    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.debug("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.debug("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.debug("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.debug("JWT claims string is empty.");
        }
        return false;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .parseClaimsJws(token)
                .getBody();
    }
    public String extractId(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("userId", String.class);
    }
}
