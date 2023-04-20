package com.duneyrefrigeracao.backend.infrastructure.encryption;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@SpringBootTest
public class EncrypterTest {

    @Test
    public void deveGerarUmSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[128];
        random.nextBytes(salt);

        String saltString = new String(salt, StandardCharsets.UTF_8);

        Assertions.assertNotNull(saltString);
    }

    @Test
    public void deveCriptografarUmaSenha() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String valor = "JUNIT_TEMPLATE_PASSWORD";

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[128];
        random.nextBytes(salt);

        KeySpec spec = new PBEKeySpec(valor.toCharArray(), salt, 500000, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        String passwordENcrypted = Base64.getEncoder().encodeToString(hash);

        Assertions.assertNotNull(passwordENcrypted);
    }

    @Test
    public void deveCriptografarUmaSenhaComSaltEstabelecido() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String valor = "JUNIT_TEMPLATE_PASSWORD";
        String salt = "Salgado";

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(valor.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), 500000, 256*8);
        byte[] hash = factory.generateSecret(spec).getEncoded();
        String passwordENcrypted = Base64.getEncoder().encodeToString(hash);

        Assertions.assertNotNull(passwordENcrypted);
    }

    @Test
    public void deveGerarAMesmaSenhaComSaltEstabelecido() throws NoSuchAlgorithmException, InvalidKeySpecException {

        String valor = "JUNIT_TEMPLATE_PASSWORD";
        String salt = "Salgado";

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(valor.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), 500000, 256*8);
        byte[] hash = factory.generateSecret(spec).getEncoded();
        String passwordENcrypted = Base64.getEncoder().encodeToString(hash);

        spec = new PBEKeySpec(valor.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), 500000, 256*8);
        hash = factory.generateSecret(spec).getEncoded();

        String passwordEncrypted2 = Base64.getEncoder().encodeToString(hash);

        Assertions.assertEquals(passwordENcrypted,passwordEncrypted2);

    }

    @Test
    public void deveGerarSenhasDiferentesComSaltsDiferentes() throws NoSuchAlgorithmException, InvalidKeySpecException {

        String valor = "JUNIT_TEMPLATE_PASSWORD";
        String salt = "Salgado";

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(valor.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), 500000, 256*8);
        byte[] hash = factory.generateSecret(spec).getEncoded();
        String passwordENcrypted = Base64.getEncoder().encodeToString(hash);

        spec = new PBEKeySpec(valor.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), 500000, 256*8);
        hash = factory.generateSecret(spec).getEncoded();

        String passwordEncrypted2 = Base64.getEncoder().encodeToString(hash);

        Assertions.assertEquals(passwordENcrypted,passwordEncrypted2);

    }

}
