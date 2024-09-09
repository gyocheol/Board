package com.board.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    @Value("${spring.security.securityKey}")
    private String secret_key;
    private static final long accessToken_expiration = 24 * 60 * 60 * 1000; // 1일
    private static final long refreshToken_expiration = accessToken_expiration * 7; // 7일
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * AccessToken 생성
     * @param username
     * @return
     */
    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessToken_expiration))
                .signWith(SignatureAlgorithm.HS256, secret_key)     // 성능은 HS256, 보안 안정성은 HS512
                .compact();
    }

    /**
     * RefreshToken 생성 및 저장
     * @param username
     * @return
     */
    public String generateRefreshToken(String username) {
        String refreshToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshToken_expiration))
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();

        // Redis에 Refresh Token 저장
        redisTemplate.opsForValue().set(username, refreshToken, refreshToken_expiration, TimeUnit.MILLISECONDS);
        return refreshToken;
    }

    /**
     * Token 정보 추출
     * @param token
     * @return
     */
    public Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token).getBody();
    }

    /**
     * Token에서 ID(Username) 추출
     * @param token
     * @return
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Token 사용자 유효성 검사
     * @param token
     * @param username
     * @return
     */
    public boolean isTokenValid(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * Token 기간 유효성 검사
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    /**
     * RefreshToken 유효성 검사 및 만료 검사
     * @param username
     * @param refreshToken
     * @return
     */
    public boolean validateRefreshToken(String username, String refreshToken) {
        String storedToken = (String) redisTemplate.opsForValue().get(username);
        return storedToken != null && storedToken.equals(refreshToken) && !isTokenExpired(refreshToken);
    }

    /**
     * Token 삭제
     * @param username
     */
    public void deleteRefreshToken(String username) {
        redisTemplate.delete(username);
    }
}
