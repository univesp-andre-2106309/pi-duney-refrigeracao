package com.duneyrefrigeracao.backend.presentation.controller;


import com.duneyrefrigeracao.backend.application.dataobject.request.account.PostUpdateAccountReq;
import com.duneyrefrigeracao.backend.application.dataobject.request.account.PostUpdatePasswordReq;
import com.duneyrefrigeracao.backend.application.mapper.AccountMapper;
import com.duneyrefrigeracao.backend.application.service.IAccountService;
import com.duneyrefrigeracao.backend.domain.enums.LogLevel;
import com.duneyrefrigeracao.backend.domain.exception.*;
import com.duneyrefrigeracao.backend.application.dataobject.generic.ExceptionResponse;
import com.duneyrefrigeracao.backend.application.dataobject.request.account.PostCreateAccountReq;
import com.duneyrefrigeracao.backend.application.dataobject.request.account.PostValidateLoginReq;
import com.duneyrefrigeracao.backend.application.dataobject.response.account.PostCreateAccountResp;
import com.duneyrefrigeracao.backend.application.dataobject.response.account.PostValidateLoginResp;
import com.duneyrefrigeracao.backend.domain.model.Account;
import com.duneyrefrigeracao.backend.infrastructure.logging.ILogging;
import com.duneyrefrigeracao.backend.infrastructure.logging.Logging;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final IAccountService _accountService;
    private final ILogging _logging;

    public AccountController(IAccountService accountService) {
        this._accountService = accountService;
        this._logging = new Logging(AccountController.class);

    }

    @PostMapping("/create")
    public ResponseEntity<Object> PostPage(@RequestBody PostCreateAccountReq request) {
        try {
            this._logging.LogMessage(LogLevel.INFO, "Executando inserção de uma nova account");
            this._logging.LogMessage(LogLevel.INFO, String.format("Dados: Email: %s, Nome: %s %s, Username: %s", request.email(), request.firstName(), request.lastName(), request.username()));
            this._accountService.persistAccount(request);
            return ResponseEntity.ok().body(new PostCreateAccountResp("Novo usuario adicionado com sucesso!", request.username(), request.email()));
        } catch (AccountValidationException er) {
            return ResponseEntity.badRequest().body(new ExceptionResponse("Erro de validação", er.getMessage()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro de cripotgrafia da senha: %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> ValidateLogin(@RequestBody PostValidateLoginReq request) {
        this._logging.LogMessage(LogLevel.INFO, String.format("Iniciando tentativa de login para o email - %s", request.email()));
        try {
            if (this._accountService.verifyLogin(request.email(), request.password())) {
                String username = this._accountService.retrieveUsername(request.email());

                String token = this._accountService.generateAccountToken(username);

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + token);

                this._logging.LogMessage(LogLevel.INFO, String.format("Autenticação do login do email %s feita com sucesso!", request.email()));
                return ResponseEntity.ok().headers(headers).body(new PostValidateLoginResp("Validação feita com sucesso", true));
            }
            this._logging.LogMessage(LogLevel.INFO, String.format("Autenticação do login do email %s não pode ser feita - senha incorreta!", request.email()));
            return ResponseEntity.ok().body(new PostValidateLoginResp("Senha invalida!", false));
        } catch (AccountValidationException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Autenticação do login do email %s não pode ser feita - erro de autenticação - %s", request.email(), er.getMessage()));
            return ResponseEntity.badRequest().body(new ExceptionResponse("Erro de validação", er.getMessage()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro de cripotgrafia da senha: %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/update-account")
    public ResponseEntity<Object> UpdateAccount(@RequestBody PostUpdateAccountReq request){

        try{
            AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

            Account account = mapper.updateAccountParaAccount(request);

            this._accountService.accountUpdate(account);

            return ResponseEntity.ok().build();
        }catch (AccountNotAvailableException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro de account não disponivel: %s", er.getMessage()));
            return ResponseEntity.badRequest().body(new ExceptionResponse("Erro de conta invalida", er.getMessage()));
        } catch(EmailPatternException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro de validação do email: %s", er.getMessage()));
            return ResponseEntity.badRequest().body(new ExceptionResponse("Erro de validação", er.getMessage()));
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }


    @PatchMapping("/update-password")
    public ResponseEntity<Object> UpdatePassword(@RequestBody PostUpdatePasswordReq request) {
        try {
            this._accountService.updatePassword(request.oldPassword(), request.newPassword(), request.newPasswordCheck());
            return ResponseEntity.ok().build();
        } catch (PasswordNotEqualException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro de senha não iguais: %s", er.getMessage()));
            return ResponseEntity.badRequest().body(new ExceptionResponse("Erro de senha", er.getMessage()));
        } catch (InvalidPasswordException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro de senha invalida: %s", er.getMessage()));
            return ResponseEntity.badRequest().body(new ExceptionResponse("Erro de senha", er.getMessage()));
        } catch (AccountNotAvailableException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro de account não disponivel: %s", er.getMessage()));
            return ResponseEntity.badRequest().body(new ExceptionResponse("Erro de conta invalida", er.getMessage()));
        } catch (AccountNotFoundException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro de account não encontrada: %s", er.getMessage()));
            return ResponseEntity.badRequest().body(new ExceptionResponse("Erro de conta indisponivel", er.getMessage()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro de cripotgrafia da senha: %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }
}
