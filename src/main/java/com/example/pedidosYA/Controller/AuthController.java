package com.example.pedidosYA.Controller;


import com.example.pedidosYA.DTO.AuthDTO.LoginRequest;
import com.example.pedidosYA.DTO.AuthDTO.RegisterRequest;
import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Restaurante;
import com.example.pedidosYA.Model.RolUsuario;
import com.example.pedidosYA.Model.Usuario;
import com.example.pedidosYA.Security.JwtUtil;
import com.example.pedidosYA.Service.CustomUserDetailsService;

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
    private final CustomUserDetailsService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager, CustomUserDetailsService usuarioService,  PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = usuarioService.loadUserByUsername(request.getUsername());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toList());

        String token = JwtUtil.generarToken(userDetails.getUsername(), roles);

        return ResponseEntity.ok().body(token);
    }


    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegisterRequest request) {
        if (usuarioService.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El usuario ya existe");
        }

        Usuario nuevo;

        switch (request.getRol().toUpperCase()) {
            case "CLIENTE" -> nuevo = new Cliente();
            case "RESTAURANTE" -> nuevo = new Restaurante();
            default -> {
                return ResponseEntity.badRequest().body("Rol inválido. Usar: CLIENTE o RESTAURANTE.");
            }
        }

        nuevo.setUsuario(request.getUsername());
        nuevo.setContrasenia(passwordEncoder.encode(request.getPassword()));
        nuevo.setRol(RolUsuario.valueOf(request.getRol().toUpperCase()));

        usuarioService.save(nuevo);

        String token = JwtUtil.generarToken(
                nuevo.getUsername(),
                List.of("ROLE_" + nuevo.getRol().name())
        );

        return ResponseEntity.ok().body(token);
    }
}
