package com.epam.esm.web.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtManager {

    public boolean isNotExpiredToken(String token) {
        Date expirationDate = getExpirationDate(token);
        return  expirationDate.after(new Date());
    }

    public String getUsername(String token) {
        Claims claims = getAllClaims(token);
        return claims.getSubject();
    }

    private Date getExpirationDate(String token) {
        Claims claims = getAllClaims(token);
        return claims.getExpiration();
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor("experimentexperimentexperimentexperimentexperimentexperiment".getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    public String getToken(Authentication authResult) {
        return Jwts
                .builder()
                .setIssuedAt(new Date())
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusDays(8)))
                .signWith(Keys.hmacShaKeyFor("experimentexperimentexperimentexperimentexperimentexperiment".getBytes()))
                .compact();
    }

    public Set<? extends GrantedAuthority> getAuthorities(String token) {
        Claims claims = getAllClaims(token);
        var authorities = (List<Map<String, String>>)claims.get("authorities");
        return authorities
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.get("authority")))
                .collect(Collectors.toSet());
    }
}
