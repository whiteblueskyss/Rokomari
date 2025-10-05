package com.selim.lms.utils;

import io.jsonwebtoken.Jwts; // jwt is used for creating and parsing JWTs. It also provides methods for signing and verifying tokens. 
import io.jsonwebtoken.security.Keys; // Keys is a utility class for generating secure keys for signing JWTs.
import org.springframework.stereotype.Component; // Component is a Spring annotation that indicates that a class is a Spring-managed component. When a class is annotated with @Component, Spring will automatically detect it and create an instance of it as a bean in the application context.

import javax.crypto.SecretKey; // SecretKey is an interface that represents a secret (symmetric) key used for signing and verifying JWTs.
import java.util.Date;

@Component
public class JwtTokenUtil {
    
    // Generate a secure key for HS512 (512 bits minimum)
    private final SecretKey secretKey = Jwts.SIG.HS512.key().build();
    
    private final long JWT_EXPIRATION = 24 * 60 * 60 * 1000; // 24 hours
    
    public String generateToken(Long userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + JWT_EXPIRATION);
        
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact(); // compact() method is used to build the JWT and serialize it into a compact, URL-safe string.
    }
    
    public Long getUserIdFromToken(String token) {
        return Long.parseLong(Jwts.parser()
                .verifyWith(secretKey)
                .build() // build() method is used to create a JwtParser instance with the specified configuration.
                .parseSignedClaims(token)
                .getPayload() 
                .getSubject()); // getSubject() method retrieves the subject (user ID) from the JWT payload.
    }
    
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}