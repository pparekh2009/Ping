package com.priyanshparekh.ping.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDate;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    private final String SECRET_KEY = "UjJ4Nk1yZ3JuYWZ1SGpnb2VtcGxvY3hlcXczMTA0M0E";

    private final long EXPIRATION_IN_MS = 1000L * 60 * 60 * 24 * 30;  // Expiration time - 1 month (30 Days)

    public String generateToken(PingUserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setId(userDetails.getUser().getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_IN_MS))
                .signWith(getKey())
                .compact();
    }

    public Key getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        boolean isEqualUsername = username.equals(userDetails.getUsername());
        log.info("jwtService: isTokenValid: isEqualUsername: {}", isEqualUsername);
        boolean isTokenExpired = isTokenExpired(token);
        log.info("jwtService: isTokenValid: isTokenExpired: {}", isTokenExpired);
        return isEqualUsername && !isTokenExpired;
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extractUserId(String token) {
        return Long.parseLong(extractClaim(token, Claims::getId));
    }
}
