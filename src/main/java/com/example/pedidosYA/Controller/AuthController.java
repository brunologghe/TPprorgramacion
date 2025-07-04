package com.example.pedidosYA.Controller;


import com.example.pedidosYA.DTO.AuthDTO.LoginRequest;
import com.example.pedidosYA.DTO.AuthDTO.RegisterRequest;
import com.example.pedidosYA.Model.*;
import com.example.pedidosYA.Security.JwtUtil;
import com.example.pedidosYA.Service.AuthService;
import com.example.pedidosYA.Service.CustomUserDetailsService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final AuthService authService;

    public AuthController(AuthenticationManager authManager, AuthService authService) {
        this.authManager = authManager;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsuario(), request.getContrasenia())
        );

        String token = authService.login(request.getUsuario());
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@Valid @RequestBody RegisterRequest request) {
        String mensaje = authService.registro(request);
        return ResponseEntity.ok(mensaje);
    }

    @PostMapping("/registro-admin")
    public ResponseEntity<?> registroAdmin(@Valid @RequestBody RegisterRequest request) {
        String mensaje = authService.registrarAdmin(request);
        if (mensaje.equals("Ya existe un administrador registrado.")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensaje);
        }
        return ResponseEntity.ok(mensaje);
    }
}
