package com.corporate.luxury.luxury_corporate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


 
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()  // Permite todas las peticiones sin autenticación
            )
            .csrf(csrf -> csrf.disable())  // Deshabilita CSRF para desarrollo (NO usar en producción)
            .formLogin(form -> form.disable());  // Deshabilita el formulario de login por defecto

        return http.build();
    }
}
