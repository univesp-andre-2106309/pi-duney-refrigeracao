package com.duneyrefrigeracao.backend.infrastructure.security;

import com.duneyrefrigeracao.backend.domain.enums.LogLevel;
import com.duneyrefrigeracao.backend.infrastructure.logging.ILogging;
import com.duneyrefrigeracao.backend.infrastructure.logging.Logging;
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
    private final ILogging _logging;


    public JwtProvider(){
        this._logging = new Logging(JwtProvider.class);
    }

    public String generateToken(String username) {

        this._logging.LogMessage(LogLevel.INFO, "Gerando token JWT....");

        LocalDateTime now =  LocalDateTime.now();
        LocalDateTime expiryDate = now.plusMinutes(TOKEN_DURATION);
        String secretKey = System.getenv("JWT_SECRET");

        Map<String, Object> claims = new HashMap<>();
        claims.put("iss","duneyrefrigeracao.backend");
        claims.put("sub","duneyrefrigeracao");
        claims.put("exp",expiryDate.toEpochSecond(ZoneOffset.UTC));
        claims.put("iat", now.toEpochSecond(ZoneOffset.UTC));
        claims.put("jti", UUID.randomUUID().toString());

        this._logging.LogMessage(LogLevel.INFO, "Token gerado com sucesso!");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getUsernameFromJwt(String token) {
        try{
            String secretKey = System.getenv("JWT_SECRET");
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        }catch(Exception er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Ocorreu um erro ao recuperar os dados do token -> %s", er.getMessage()));
            throw er;
        }

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
            this._logging.LogMessage(LogLevel.INFO, "Token informado se encontra expirado!");
            return false;
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s",er.getMessage()));
            return false;
        }
    }
}
