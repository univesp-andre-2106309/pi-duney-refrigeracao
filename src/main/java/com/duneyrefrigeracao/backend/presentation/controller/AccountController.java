package com.duneyrefrigeracao.backend.presentation.controller;

import com.duneyrefrigeracao.backend.application.service.AccountService;
import com.duneyrefrigeracao.backend.domain.exception.AccountValidationException;
import com.duneyrefrigeracao.backend.presentation.dataobject.generic.ExceptionResponse;
import com.duneyrefrigeracao.backend.presentation.dataobject.request.account.PostCreateAccountReq;
import com.duneyrefrigeracao.backend.presentation.dataobject.request.account.PostValidateLoginReq;
import com.duneyrefrigeracao.backend.presentation.dataobject.response.account.PostCreateAccountResp;
import com.duneyrefrigeracao.backend.presentation.dataobject.response.account.PostValidateLoginResp;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> PostPage(@RequestBody PostCreateAccountReq request) {
        try {
            accountService.persistAccount(request);
            return ResponseEntity.ok().body(new PostCreateAccountResp("Novo usuario adicionado com sucesso!", request.username(), request.email()));
        } catch (AccountValidationException er) {
            return ResponseEntity.badRequest().body(new ExceptionResponse("Erro de validação", er.getMessage()));
        } catch (Exception er) {
            //TODO: Adicionar logs de sistema
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> ValidateLogin(@RequestBody PostValidateLoginReq request) {
        try {
            if (accountService.verifyLogin(request.email(), request.password())) {
                String username = accountService.retrieveUsername(request.email());

                String token = accountService.generateAccountToken(username);

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + token);

                return ResponseEntity.ok().headers(headers).body(new PostValidateLoginResp("Validação feita com sucesso",true));
            }
            return ResponseEntity.ok().body(new PostValidateLoginResp("Senha invalida!",false));
        } catch (AccountValidationException er) {
            return ResponseEntity.badRequest().body(new ExceptionResponse("Erro de validação", er.getMessage()));
        } catch (Exception er) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
