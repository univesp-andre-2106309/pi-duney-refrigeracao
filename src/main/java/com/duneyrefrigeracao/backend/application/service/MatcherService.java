package com.duneyrefrigeracao.backend.application.service;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class MatcherService implements IMatcherService{

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);


    public boolean validateEmailPattern(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

}
