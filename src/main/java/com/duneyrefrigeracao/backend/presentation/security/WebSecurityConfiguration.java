package com.duneyrefrigeracao.backend.presentation.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private final JwtAuthenticationFilter _jwtAuthorizationFilter;


    public WebSecurityConfiguration(JwtAuthenticationFilter _jwtAuthorizationFilter) {
        this._jwtAuthorizationFilter = _jwtAuthorizationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeHttpRequests(request ->
                        request.requestMatchers("/api/account/**").permitAll()
                                .anyRequest().authenticated()
                                .and()
                                .addFilterBefore(_jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
////                                .addFilter(_jwtAuthenticationFilter)
                        )
                .csrf().disable();

        return http.build();
    }
}
