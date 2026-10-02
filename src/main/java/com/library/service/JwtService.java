package com.library.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private  final SecretKey key;
    private  final long expirationTime;


    public JwtService(
    		               @Value("${jwt.secret}") String secret, 
    		               @Value("${jwt.expiration}") long expirationTime
    		             )
    
    {
    	  this.expirationTime = expirationTime;
    	  this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Generate JWT
    public String generateToken(Long userId, String email, String role) {

        Date now = new Date();

        Date expiration =
                new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    // Extract all claims
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    
    // Extract email
    public String extractEmail(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    // Extract role
    public String extractRole(String token) {

        return extractAllClaims(token)
                .get("role", String.class);
    }
    
    //validate token
    public boolean validateToken(String token, String email) {

        String tokenEmail = extractEmail(token);

        return tokenEmail.equals(email)
                && !isTokenExpired(token);
    }

    // Check expiration
    private boolean isTokenExpired(String token) {

        Date expiration =
                extractAllClaims(token).getExpiration();

        return expiration.before(new Date());
    }
}