package com.duneyrefrigeracao.backend.infrastructure.encryption;

import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;
import org.springframework.data.util.Pair;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface IEncrypter {

    Tuple<String, String> encryptValue(String value) throws NoSuchAlgorithmException, InvalidKeySpecException;
    Tuple<String, String> encryptValueWithSalt(String value, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException;

}
