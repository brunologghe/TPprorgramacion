package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.AuthDTO.RegisterRequest;
import com.example.pedidosYA.Model.*;
import com.example.pedidosYA.Repository.ReseniaRepository;
import com.example.pedidosYA.Repository.UsuarioRepository;
import com.example.pedidosYA.Security.JwtUtil;
import com.example.pedidosYA.Validations.AdminValidations;
import com.example.pedidosYA.Validations.ClienteValidations;
import com.example.pedidosYA.Validations.RestauranteValidations;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {


    @Autowired
    CustomUserDetailsService usuarioService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RestauranteValidations restauranteValidations;

    @Autowired
    AdminValidations adminValidations;

    @Autowired
    ClienteValidations clienteValidations;

    public String login (String usuario){
        UserDetails userDetails = usuarioService.loadUserByUsername(usuario);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toList());

        return JwtUtil.generarToken(userDetails.getUsername(), roles);
    }

    @Transactional
    public String registro(RegisterRequest request) {
        if (usuarioService.existsByUsername(request.getUsuario())) {
            throw new RuntimeException("El usuario ya existe");
        }

        Usuario nuevoUsuario;

        switch (request.getRol().toUpperCase()) {
            case "CLIENTE":
                Cliente cliente = new Cliente();

                clienteValidations.validarNombreCrear(request.getNombreYapellido());
                clienteValidations.validarUsuario(request.getUsuario());
                clienteValidations.validarContrasenia(request.getContrasenia());

                cliente.setUsuario(request.getUsuario());
                cliente.setContrasenia(passwordEncoder.encode(request.getContrasenia()));
                cliente.setNombreYapellido(request.getNombreYapellido());
                cliente.setRol(RolUsuario.CLIENTE);
                nuevoUsuario = cliente;
                break;

            case "RESTAURANTE":
                Restaurante restaurante = new Restaurante();

                restauranteValidations.validarNombreCrear(request.getNombreRestaurante());
                restauranteValidations.validarUsuario(request.getUsuario());
                restauranteValidations.validarContrasenia(request.getContrasenia());

                restaurante.setUsuario(request.getUsuario());
                restaurante.setContrasenia(passwordEncoder.encode(request.getContrasenia()));
                restaurante.setNombre(request.getNombreRestaurante());
                restaurante.setRol(RolUsuario.RESTAURANTE);
                nuevoUsuario = restaurante;
                break;

            default:
                throw new RuntimeException("Solo se pueden registrar usuarios con rol CLIENTE o RESTAURANTE.");
        }

        usuarioService.save(nuevoUsuario);
        return nuevoUsuario.getRol().name() + " creado";
    }


    public String registrarAdmin(RegisterRequest request) {
        if (usuarioService.existsByRol(RolUsuario.ADMIN)) {
            return "Ya existe un administrador registrado.";
        }

        Admin admin = new Admin();

        adminValidations.validarUsuario(request.getUsuario());
        adminValidations.validarContrasenia(request.getContrasenia());

        admin.setUsuario(request.getUsuario());
        admin.setContrasenia(passwordEncoder.encode(request.getContrasenia()));
        admin.setRol(RolUsuario.ADMIN);

        usuarioService.save(admin);
        return "ADMIN creado";
    }
}
