package com.crazydream.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;

/**
 * JWT工具类
 * 用于生成和验证JWT令牌
 * 
 * @author CrazyDream Team
 * @since 2025-12-10
 */
@Component
public class JwtUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private long expiration;
    
    @Value("${jwt.header}")
    private String header;
    
    @Value("${jwt.prefix}")
    private String prefix;
    
    /**
     * 获取JWT密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * 生成JWT令牌
     */
    public String generateToken(Long userId, String username) {
        logger.info("为用户生成JWT令牌，用户ID: {}, 用户名: {}", userId, username);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration * 1000);
        logger.debug("JWT令牌过期时间: {}", expirationDate);
        
        String token = Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
        
        logger.info("JWT令牌生成成功，用户ID: {}", userId);
        return token;
    }
    
    /**
     * 解析JWT令牌
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            logger.error("JWT令牌解析失败: {}", e.getMessage(), e);
            throw new RuntimeException("无效的JWT令牌", e);
        }
    }
    
    /**
     * 从JWT令牌中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }
    
    /**
     * 从JWT令牌中获取用户名
     */
    public String extractUsername(String token) {
        Claims claims = parseToken(token);
        return claims.get("username", String.class);
    }
    
    /**
     * 验证JWT令牌
     */
    public boolean validateToken(String token) {
        logger.debug("开始验证JWT令牌");
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            logger.error("JWT令牌验证失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 验证JWT令牌（带用户信息）
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            logger.error("JWT令牌验证失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 检查令牌是否过期
     */
    private boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().before(new Date());
    }
    
    /**
     * 从Authorization头中提取JWT令牌
     */
    public String extractTokenFromHeader(String headerValue) {
        if (headerValue != null && headerValue.startsWith(prefix)) {
            return headerValue.substring(prefix.length());
        }
        return null;
    }
    
    public String getHeader() {
        return header;
    }
    
    public String getPrefix() {
        return prefix;
    }
}