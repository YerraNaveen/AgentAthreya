package com.agentathreya.conversationservice.security;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private  SecretKey key;
    private long jwtExpiration;

    public JwtService(@Value("${jwt.secret}") String secretKey, 
                   @Value("${jwt.expiration}") long jwtExpiration) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.jwtExpiration = jwtExpiration;
    }

    // Generate JWT Token
    public String generateToken(String username) {
        return Jwts.builder()
                .claim("username", username) // Use "username" instead of "sub"
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))  // Use config expiration time
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    // Refresh JWT Token
    public String refreshToken(String token) {
        String username = extractUsername(token);
        return generateToken(username); // Generate a new token with the same username
    }

    // Update expiration time dynamically
    public void setJwtExpiration(long jwtExpiration) {
        this.jwtExpiration = jwtExpiration;
    }

    // Extract Username
    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.get("username", String.class)); // Extract "username" claim
    }

    // Extract Any Claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract All Claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Validate Token
    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    // Check if Token Expired
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}