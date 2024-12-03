package com.example.nrfaboekhoudapplicatie.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configureer CORS
                .csrf(csrf -> csrf.disable()) // Schakel CSRF-bescherming uit voor REST API's
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Gebruik stateless sessies
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll() // Sta openbare toegang toe tot Swagger en API-documentatie
                        .requestMatchers("/h2-console/**").permitAll() // Sta openbare toegang toe tot H2-console
                        .requestMatchers("/api/auth/client/login", "/api/auth/accountant/login").permitAll() // Sta openbare toegang toe tot inlogroutes
                        .requestMatchers("/api/**").permitAll() // Sta openbare toegang toe tot alle API-routes
                        .anyRequest().permitAll() // Sta openbare toegang toe tot alle andere verzoeken
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()) // Sta framing toe vanaf dezelfde oorsprong voor H2-console
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173")); // Stel toegestane oorsprong in
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Stel toegestane HTTP-methoden in
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Stel toegestane headers in
        corsConfiguration.setAllowCredentials(true); // Sta credentials toe
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // Registreer CORS-configuratie voor alle paden
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Definieer BCryptPasswordEncoder bean
    }
}
