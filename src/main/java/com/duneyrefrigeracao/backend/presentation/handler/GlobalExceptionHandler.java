package com.duneyrefrigeracao.backend.presentation.handler;

import com.duneyrefrigeracao.backend.application.dataobject.generic.ExceptionResponse;
import com.duneyrefrigeracao.backend.domain.enums.LogLevel;
import com.duneyrefrigeracao.backend.infrastructure.logging.ILogging;
import com.duneyrefrigeracao.backend.infrastructure.logging.Logging;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final ILogging _logging;
    public GlobalExceptionHandler() {
        this._logging = new Logging(GlobalExceptionHandler.class);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {

        this._logging.LogMessage(LogLevel.WARN, String.format("Erro de tipagem no argumento %s",ex.getMessage()));

        return ResponseEntity.badRequest().body(new ExceptionResponse(
                "Erro ao informar parametros",
                ex.getMessage()
        ));
    }
}
