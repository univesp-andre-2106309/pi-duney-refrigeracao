package com.duneyrefrigeracao.backend.application.service;

import org.springframework.stereotype.Service;


public interface IMatcherService {
    boolean validateEmailPattern(String email);
}
