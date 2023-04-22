package com.duneyrefrigeracao.backend.application.service;

import com.duneyrefrigeracao.backend.application.dataobject.request.account.PostCreateAccountReq;
import com.duneyrefrigeracao.backend.domain.model.Account;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface IAccountService {

    Account persistAccount(PostCreateAccountReq accountReq) throws NoSuchAlgorithmException, InvalidKeySpecException;
    boolean verifyLogin(String email, String password) throws NoSuchAlgorithmException, InvalidKeySpecException;
    String retrieveUsername(String email);
    String generateAccountToken(String userName);
    UserDetails loadUserDetailsByUsername(String username);
}
