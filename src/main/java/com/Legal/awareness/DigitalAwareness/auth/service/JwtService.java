package com.Legal.awareness.DigitalAwareness.auth.service;

import com.Legal.awareness.DigitalAwareness.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${JWT.SECRET}")
    private String jwtToken;

    @Value("${JWT.EXPIRATION}")
    private long expirationTime;


    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        return username.equals(user.getEmail()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public String generateToken(User users) {
        return Jwts.builder()
                .subject(users.getEmail())
                .claim("role", users.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() +    expirationTime))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private Key  getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtToken);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
