package com.faq.chatbot.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utility class for JWT token generation and validation
 */
@Component
public class JwtUtil {
    
    @Value("${jwt.secret:your-secret-key-change-this-in-production}")
    private String secretKey;
    
    @Value("${jwt.expiration:86400000}")
    private long expiration;
    
    /**
     * Generate JWT token for admin user
     *
     * @param adminEmail Admin email
     * @param adminId Admin ID
     * @return JWT token
     */
    public String generateToken(String adminEmail, Long adminId) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        
        return Jwts.builder()
                .setSubject(adminEmail)
                .claim("adminId", adminId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Get email from JWT token
     *
     * @param token JWT token
     * @return Admin email
     */
    public String getEmailFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    /**
     * Validate JWT token
     *
     * @param token JWT token
     * @return True if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
