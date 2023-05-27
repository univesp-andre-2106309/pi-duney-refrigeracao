package com.duneyrefrigeracao.backend.presentation.filter;

import com.duneyrefrigeracao.backend.application.service.IAccountService;
import com.duneyrefrigeracao.backend.domain.model.RefreshToken;
import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;
import com.duneyrefrigeracao.backend.infrastructure.security.IJwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {


    private final IJwtProvider _jwtProvider;
    private final IAccountService _accountService;
    private static final List<String> SKIP_ENDPOINTS = Arrays.asList("/api/account/login","/api/account/create", "/api/account/validate");

    public JwtAuthorizationFilter(@Lazy AuthenticationManager authenticationManager, IJwtProvider _jwtProvider, IAccountService _accountService) {
        super(authenticationManager);
        this._jwtProvider = _jwtProvider;
        this._accountService = _accountService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String refreshToken = request.getHeader("RefreshToken");

        if (SKIP_ENDPOINTS.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        //if (header == null || !header.startsWith("Bearer ")) {
        if (header == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");
        UsernamePasswordAuthenticationToken auth;
        UserDetails userDetails;

        try {
            auth = authenticate(token);
            if (auth == null) {
                filterChain.doFilter(request, response);
                return;
            }

            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException er) {

            if(refreshToken == null) {
                filterChain.doFilter(request, response);
                return;
            }

            Tuple<RefreshToken, String> newRefreshTokenTpl = this._jwtProvider.validateRefreshToken(refreshToken);

            if (newRefreshTokenTpl == null) {
                filterChain.doFilter(request, response);
                return;
            }

            userDetails = _accountService.loadUserDetailsByUsername(newRefreshTokenTpl.getFirstValue().getAccount().getUsername());
            auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            response.addHeader("Authorization", newRefreshTokenTpl.getSecondValue());
            response.addHeader("RefreshToken", newRefreshTokenTpl.getFirstValue().getRefreshToken());

            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } catch (Exception er) {
            filterChain.doFilter(request, response);
        }
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
