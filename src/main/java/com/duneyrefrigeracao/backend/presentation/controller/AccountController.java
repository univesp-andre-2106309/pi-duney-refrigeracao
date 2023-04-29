package com.duneyrefrigeracao.backend.presentation.controller;


import com.duneyrefrigeracao.backend.application.service.IAccountService;
import com.duneyrefrigeracao.backend.domain.enums.LogLevel;
import com.duneyrefrigeracao.backend.domain.exception.AccountValidationException;
import com.duneyrefrigeracao.backend.application.dataobject.generic.ExceptionResponse;
import com.duneyrefrigeracao.backend.application.dataobject.request.account.PostCreateAccountReq;
import com.duneyrefrigeracao.backend.application.dataobject.request.account.PostValidateLoginReq;
import com.duneyrefrigeracao.backend.application.dataobject.response.account.PostCreateAccountResp;
import com.duneyrefrigeracao.backend.application.dataobject.response.account.PostValidateLoginResp;
import com.duneyrefrigeracao.backend.infrastructure.logging.ILogging;
import com.duneyrefrigeracao.backend.infrastructure.logging.Logging;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final IAccountService accountService;
    private static final Logger logger = LogManager.getLogger(AccountController.class);

    private final ILogging _logging;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
        this._logging = new Logging(AccountController.class);

    }

    @PostMapping("/create")
    public ResponseEntity<Object> PostPage(@RequestBody PostCreateAccountReq request) {
        try {
            this._logging.LogMessage(LogLevel.INFO, "Executando inserção de uma nova account");
            this._logging.LogMessage(LogLevel.INFO, String.format("Dados: Email: %s, Nome: %s %s, Username: %s",request.email(), request.firstName(), request.lastName(), request.username()));
            accountService.persistAccount(request);
            return ResponseEntity.ok().body(new PostCreateAccountResp("Novo usuario adicionado com sucesso!", request.username(), request.email()));
        } catch (AccountValidationException er) {

            return ResponseEntity.badRequest().body(new ExceptionResponse("Erro de validação", er.getMessage()));
        } catch (Exception er) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> ValidateLogin(@RequestBody PostValidateLoginReq request) {
        this._logging.LogMessage(LogLevel.INFO, String.format("Iniciando tentativa de login para o email - %s", request.email()));
        try {
            if (accountService.verifyLogin(request.email(), request.password())) {
                String username = accountService.retrieveUsername(request.email());

                String token = accountService.generateAccountToken(username);

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + token);

                this._logging.LogMessage(LogLevel.INFO, String.format("Autenticação do login do email %s feita com sucesso!", request.email()));
                return ResponseEntity.ok().headers(headers).body(new PostValidateLoginResp("Validação feita com sucesso",true));
            }
            this._logging.LogMessage(LogLevel.INFO, String.format("Autenticação do login do email %s não pode ser feita - senha incorreta!", request.email()));
            return ResponseEntity.ok().body(new PostValidateLoginResp("Senha invalida!",false));
        } catch (AccountValidationException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Autenticação do login do email %s não pode ser feita - erro de autenticação - %s", request.email(), er.getMessage()));
            return ResponseEntity.badRequest().body(new ExceptionResponse("Erro de validação", er.getMessage()));
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }
}
