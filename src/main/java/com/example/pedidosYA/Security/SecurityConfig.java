package com.example.pedidosYA.Security;

import com.example.pedidosYA.Service.AdminService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final AdminService adminService;

    public SecurityConfig(AdminService adminService) {
        this.adminService = adminService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Desactivamos CSRF (por ser API REST)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").permitAll()
                        .requestMatchers("/admin/cliente/**").permitAll()// Registro público
                        .requestMatchers("/cliente/**").permitAll()      // GET público
                        .requestMatchers("/api/products/**").authenticated() // Cualquier otra (ej. POST)
                        .anyRequest().authenticated()
                )
                .userDetailsService(adminService)
                .httpBasic() // Activamos autenticación básica
                .and()
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    // Necesario si más adelante se usa AuthenticationManager (opcional por ahora)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
