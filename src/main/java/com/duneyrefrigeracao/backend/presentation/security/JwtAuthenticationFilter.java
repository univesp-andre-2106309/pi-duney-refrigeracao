package com.duneyrefrigeracao.backend.presentation.security;

import com.duneyrefrigeracao.backend.application.service.IAccountService;
import com.duneyrefrigeracao.backend.infrastructure.security.IJwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {


    private final IJwtProvider _jwtProvider;
    private final IAccountService _accountService;

    public JwtAuthenticationFilter(@Lazy AuthenticationManager authenticationManager, IJwtProvider _jwtProvider, IAccountService _accountService) {
        super(authenticationManager);
        this._jwtProvider = _jwtProvider;
        this._accountService = _accountService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authentication");

        //if (header == null || !header.startsWith("Bearer ")) {
        if (header == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");

        UsernamePasswordAuthenticationToken auth = authenticate(token);

        //TODO: Adicionar funcionalidade refresh token.

        if (auth == null) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }


    public UsernamePasswordAuthenticationToken authenticate(String token) {

        if (_jwtProvider.checkTokenValidation(token)) {
            String username = _jwtProvider.getUsernameFromJwt(token);
            UserDetails userDetails = _accountService.loadUserDetailsByUsername(username);

            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }
        return null;
    }
}
