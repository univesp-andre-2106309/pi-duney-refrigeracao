package com.duneyrefrigeracao.backend.infrastructure.encryption;

import com.duneyrefrigeracao.backend.domain.enums.LogLevel;
import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;
import com.duneyrefrigeracao.backend.infrastructure.logging.ILogging;
import com.duneyrefrigeracao.backend.infrastructure.logging.Logging;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class Encrypter implements IEncrypter{

    private static final int SALT_LENTH = 128;
    private static final int INTERATION_COUNT = 10000;
    private static final int KEY_LENGTH = 256;

    private final ILogging _logging;

    public Encrypter(){
        this._logging = new Logging(Encrypter.class);
    }


    @Override
    public Tuple<String, String> encryptValue(String value) throws NoSuchAlgorithmException, InvalidKeySpecException {

        this._logging.LogMessage(LogLevel.INFO,"Criptografando senha......");
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENTH];
        random.nextBytes(salt);

        KeySpec spec = new PBEKeySpec(value.toCharArray(), salt, INTERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] hash = factory.generateSecret(spec).getEncoded();
        String passwordEncrypted = Base64.getEncoder().encodeToString(hash);

        this._logging.LogMessage(LogLevel.INFO,"Senha criptografada!");

        return new Tuple<>(passwordEncrypted, Base64.getEncoder().encodeToString(salt));
    }

    @Override
    public Tuple<String, String> encryptValueWithSalt(String value, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this._logging.LogMessage(LogLevel.INFO,String.format("Criptografando senha com salt %s", salt));

        KeySpec spec = new PBEKeySpec(value.toCharArray(), Base64.getDecoder().decode(salt), INTERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] hash = factory.generateSecret(spec).getEncoded();
        String passwordEncrypted = Base64.getEncoder().encodeToString(hash);

        this._logging.LogMessage(LogLevel.INFO,"Senha criptografada!");

        return new Tuple<>(passwordEncrypted, salt);
    }
}
