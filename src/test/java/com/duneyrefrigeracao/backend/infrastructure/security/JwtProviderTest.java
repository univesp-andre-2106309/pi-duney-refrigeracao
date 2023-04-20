package com.duneyrefrigeracao.backend.infrastructure.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;
import java.util.Date;

@SpringBootTest
public class JwtProviderTest {

    @Test
    public void deveGerarUmToken() {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000);
        String jwt = Jwts.builder()
                .setSubject("Usuario")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, "6173b797c7f44cc424185884e86deef5cc60a46c8a96a786707e91a88d9b1f3c6b6114ed91d76105fde4d6f410c420dbb807fda9ca7c2e2b4cfbcad8cf1a7d53")
                .compact();

        Assertions.assertNotNull(jwt);
    }

}
