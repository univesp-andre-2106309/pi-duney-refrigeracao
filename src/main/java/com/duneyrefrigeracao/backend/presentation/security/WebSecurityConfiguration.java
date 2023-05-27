package com.duneyrefrigeracao.backend.presentation.security;

import com.duneyrefrigeracao.backend.presentation.filter.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private final JwtAuthorizationFilter _jwtAuthorizationFilter;
    private final AuthEntrypoint _authEntrypoint;


    public WebSecurityConfiguration(JwtAuthorizationFilter _jwtAuthorizationFilter, AuthEntrypoint authEntrypoint) {
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
                        {
                            try {
                                request

                                        .requestMatchers("/api/account/login", "/api/account/create", "/api/account/validate").permitAll()
                                        .anyRequest().authenticated()
                                        .and()
                                        .cors()
                                        .and()
                                        .addFilterBefore(this._jwtAuthorizationFilter, BasicAuthenticationFilter.class);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        )
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
       // configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
