package com.example.pedidosYA.Controller;


import com.example.pedidosYA.DTO.AuthDTO.LoginRequest;
import com.example.pedidosYA.DTO.AuthDTO.RegisterRequest;
import com.example.pedidosYA.Model.*;
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
                new UsernamePasswordAuthenticationToken(request.getUsuario(), request.getContrasenia())
        );

        UserDetails userDetails = usuarioService.loadUserByUsername(request.getUsuario());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toList());

        String token = JwtUtil.generarToken(userDetails.getUsername(), roles);

        return ResponseEntity.ok().body(token);
    }


    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegisterRequest request) {
        if (usuarioService.existsByUsername(request.getUsuario())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El usuario ya existe");
        }

        if (request.getRol().equalsIgnoreCase("CLIENTE")) {
            Cliente cliente = new Cliente();
            cliente.setUsuario(request.getUsuario());
            cliente.setContrasenia(passwordEncoder.encode(request.getContrasenia()));
            cliente.setNombreYapellido(request.getNombreYapellido());
            cliente.setRol(RolUsuario.CLIENTE);
            usuarioService.save(cliente);
            return ResponseEntity.ok("Cliente creado");
        } else if (request.getRol().equalsIgnoreCase("RESTAURANTE")) {
            Restaurante restaurante = new Restaurante();
            restaurante.setUsuario(request.getUsuario());
            restaurante.setContrasenia(passwordEncoder.encode(request.getContrasenia()));
            restaurante.setNombre(request.getNombreRestaurante());
            restaurante.setRol(RolUsuario.RESTAURANTE);
            usuarioService.save(restaurante);
            return ResponseEntity.ok("Restaurante creado" +restaurante.getNombre());
        } else {
            // admin o usuario normal
            Usuario usuario = new Admin();
            usuario.setUsuario(request.getUsuario());
            usuario.setContrasenia(passwordEncoder.encode(request.getContrasenia()));
            usuario.setRol(RolUsuario.ADMIN);
            usuarioService.save(usuario);
            return ResponseEntity.ok("Admin creado");
        }

    }
}
