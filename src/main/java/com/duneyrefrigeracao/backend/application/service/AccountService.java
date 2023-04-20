package com.duneyrefrigeracao.backend.application.service;

import com.duneyrefrigeracao.backend.domain.exception.AccountValidationException;
import com.duneyrefrigeracao.backend.domain.model.Account;
import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;
import com.duneyrefrigeracao.backend.infrastructure.encryption.IEncrypter;
import com.duneyrefrigeracao.backend.infrastructure.repository.IUnitOfWork;
import com.duneyrefrigeracao.backend.infrastructure.security.JwtProvider;
import com.duneyrefrigeracao.backend.presentation.dataobject.request.account.PostCreateAccountReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class AccountService {

    private final IUnitOfWork _unitOfWork;
    private final IEncrypter _encrypter;

    private final JwtProvider _jwtProvider;

    public AccountService(IUnitOfWork _unitOfWork, IEncrypter _encrypter, JwtProvider _jwtProvider) {
        this._unitOfWork = _unitOfWork;
        this._encrypter = _encrypter;
        this._jwtProvider = _jwtProvider;
    }

    public Account persistAccount(PostCreateAccountReq accountReq) throws NoSuchAlgorithmException, InvalidKeySpecException {
         try{
             Account accUserVal = _unitOfWork.getAccountRepository().findAccountByUsername(accountReq.username());
             Account accEmailVal = _unitOfWork.getAccountRepository().findAccountByEmail(accountReq.email());

             if(accUserVal != null) {
                 throw new AccountValidationException("Username", "Nome de usuario já existe no sistema!");
             }

             if(accEmailVal != null) {
                 throw new AccountValidationException("Email", "Email já existe no sistema!");
             }

             Account account = new Account();

             account.setFirstName(accountReq.firstName());
             account.setLastName(accountReq.lastName());
             account.setUsername(accountReq.username());
             account.setEmail(accountReq.email());
             account.setBirthDate(accountReq.birthDate());
             account.setIsEnabled(true);




             Tuple<String,String> saltPassword = _encrypter.encryptValue(accountReq.password());

             account.setPassword(saltPassword.getFirstValue());
             account.setSalt(saltPassword.getSecondValue());

             account.setSigningDate(new Date());


             _unitOfWork.getAccountRepository().save(account);

             return account;
         }catch(AccountValidationException ave) {
             //TODO: Refazer os exceptions de forma mais padronizada
             ave.printStackTrace();
             throw ave;
         } catch(Exception er) {
             er.printStackTrace();
             throw er;
         }

    }

    public boolean verifyLogin(String email, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
       Account account = _unitOfWork.getAccountRepository().findAccountByEmail(email);

       if(account == null) {
           throw new AccountValidationException("email", "Não existe uma conta vinculada ao email informado");
       }

       String hashPassword = _encrypter.encryptValueWithSalt(password,account.getSalt()).getFirstValue();


       return account.getPassword().equals(hashPassword);

    }

    public String retrieveUsername(String email) {
        return this._unitOfWork.getAccountRepository().findAccountByEmail(email).getUsername();
    }
    public String generateAccountToken(String userName){
        return _jwtProvider.generateToken(userName);
    }

    public UserDetails loadUserDetailsByUsername(String username) {
        Account account = _unitOfWork.getAccountRepository().findAccountByUsername(username);

        if(account != null) {
            return new User(
                    account.getUsername(),
                    account.getPassword(),
                    new ArrayList<>()
            );
        }
        return null;
    }
}
