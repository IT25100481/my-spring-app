package com.example.my_spring_app.security;

import com.example.my_spring_app.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.my_spring_app.UserService;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret:your_super_secret_key_that_should_be_at_least_256_bits_long_for_security}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private int jwtExpiration;

    public String generateToken(Authentication authentication) {
        return generateTokenFromEmail(authentication.getName());
    }

    public String generateTokenFromEmail(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public String generateTokenFromUser(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            Object userId = claims.get("userId");
            if (userId instanceof Number) {
                return ((Number) userId).longValue();
            }
            if (userId instanceof String) {
                return Long.valueOf((String) userId);
            }
            return null;
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    public String getUserEmailFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            System.err.println("Invalid JWT signature: " + e);
        } catch (MalformedJwtException e) {
            System.err.println("Invalid JWT token: " + e);
        } catch (ExpiredJwtException e) {
            System.err.println("Expired JWT token: " + e);
        } catch (UnsupportedJwtException e) {
            System.err.println("Unsupported JWT token: " + e);
        } catch (IllegalArgumentException e) {
            System.err.println("JWT claims string is empty: " + e);
        }
        return false;
    }

    // Return the user id associated with the token's subject (email), or null if not found
    public Long getUserIdFromToken(String token) {
        try {
            String email = getUserEmailFromToken(token);
            if (email == null) return null;
            return userService.findByEmail(email).map(u -> u.getId()).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    @Autowired
    private UserService userService;
}
