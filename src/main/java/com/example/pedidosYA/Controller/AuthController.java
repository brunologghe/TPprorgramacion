package com.example.pedidosYA.Controller;


import com.example.pedidosYA.DTO.AuthDTO.LoginRequest;
import com.example.pedidosYA.Security.JwtUtil;
import com.example.pedidosYA.Service.CustomUserDetailsService;

import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final CustomUserDetailsService usuarioService;

    public AuthController(AuthenticationManager authManager, CustomUserDetailsService usuarioService) {
        this.authManager = authManager;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = usuarioService.loadUserByUsername(request.getUsername());
        String token = JwtUtil.generarToken(userDetails.getUsername(), List.of("ROLE_CLIENTE"));

        return ResponseEntity.ok().body(token);
    }
}
