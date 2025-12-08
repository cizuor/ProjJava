package com.openclassroom.apiProj2.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.openclassroom.apiProj2.security.JwtAuthenticationFilter;


@Configuration 
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Désactiver la protection CSRF (inutile pour les API REST stateless)
            .csrf(csrf -> csrf.disable())
            // 2. Configurer le CORS pour accepter les requêtes d'Angular
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 3. Gérer les sessions (STATELESS car on utilise des JWT, pas de session utilisateur)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 4. Autoriser l'accès aux routes d'authentification
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login","/api/auth/register",
                "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                "/images/**").permitAll() 
                .anyRequest().authenticated()
            ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean AuthenticationManager fourni par Spring AuthenticationConfiguration
    @Bean
    public AuthenticationManager authenticationManager(  AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Autoriser ton frontend Angular (modifie le port si ce n'est pas 4200)
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); 
        // Autoriser les méthodes HTTP
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Autoriser les en-têtes (Authorization, Content-Type, etc.)
        configuration.setAllowedHeaders(List.of("*"));
        // Autoriser l'envoi de cookies/credentials si besoin
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
