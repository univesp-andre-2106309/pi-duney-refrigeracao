package com.duneyrefrigeracao.backend.infrastructure.security;

public interface IJwtProvider {

    String generateToken(String username);
    String getUsernameFromJwt(String token);
    boolean checkTokenValidation(String token);
}
