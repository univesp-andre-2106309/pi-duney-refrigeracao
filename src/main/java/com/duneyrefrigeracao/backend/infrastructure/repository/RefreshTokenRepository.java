package com.duneyrefrigeracao.backend.infrastructure.repository;

import com.duneyrefrigeracao.backend.domain.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Calendar;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query("SELECT rf FROM RefreshToken rf " +
            "WHERE rf.refreshToken = :token " +
            "AND rf.isAvailable = true " +
            "AND rf.expirationDate > :dtAtual")
    RefreshToken findRefreshTokenByParams(@Param("token") String token,@Param("dtAtual") Calendar dtAtual);

}
