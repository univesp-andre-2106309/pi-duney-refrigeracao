package com.duneyrefrigeracao.backend.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtProvider implements IJwtProvider {

    //Definido por minutos
    private static final int TOKEN_DURATION = 15;

    public String generateToken(String username) {
        LocalDateTime now =  LocalDateTime.now();
        LocalDateTime expiryDate = now.plusMinutes(TOKEN_DURATION);
        String secretKey = System.getenv("JWT_SECRET");

        Map<String, Object> claims = new HashMap<>();
        claims.put("iss","duneyrefrigeracao.backend");
        claims.put("sub","duneyrefrigeracao");
        claims.put("exp",expiryDate.toEpochSecond(ZoneOffset.UTC));
        claims.put("iat", now.toEpochSecond(ZoneOffset.UTC));
        claims.put("jti", UUID.randomUUID().toString());


        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getUsernameFromJwt(String token) {
        String secretKey = System.getenv("JWT_SECRET");
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean checkTokenValidation(String token){
        try{
            String secretKey = System.getenv("JWT_SECRET");
            Date expiryDate = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return true;
        } catch(ExpiredJwtException er) {
            return false;
        }
    }
}
