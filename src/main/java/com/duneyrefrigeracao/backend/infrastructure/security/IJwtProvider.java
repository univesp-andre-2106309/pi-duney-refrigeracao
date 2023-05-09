package com.duneyrefrigeracao.backend.infrastructure.security;

import com.duneyrefrigeracao.backend.domain.model.RefreshToken;
import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;

public interface IJwtProvider {

    String generateToken(String username);
    String getUsernameFromJwt(String token);
    boolean checkTokenValidation(String token);
    Tuple<RefreshToken, String> validateRefreshToken(String refreshToken);
    RefreshToken generateRefreshToken(String token, String username);
}
