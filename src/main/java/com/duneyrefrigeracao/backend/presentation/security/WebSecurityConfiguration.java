package com.duneyrefrigeracao.backend.presentation.security;

import com.duneyrefrigeracao.backend.presentation.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private final JwtAuthenticationFilter _jwtAuthorizationFilter;
    private final AuthEntrypoint _authEntrypoint;


    public WebSecurityConfiguration(JwtAuthenticationFilter _jwtAuthorizationFilter, AuthEntrypoint authEntrypoint) {
        this._jwtAuthorizationFilter = _jwtAuthorizationFilter;
        this._authEntrypoint = authEntrypoint;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(this._authEntrypoint)
                .and()
                .authorizeHttpRequests(request ->
                        request.requestMatchers("/api/account/**").permitAll()
                                .anyRequest().authenticated()
                                .and()
                                .addFilterBefore(this._jwtAuthorizationFilter, BasicAuthenticationFilter.class)
                        )
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
}
