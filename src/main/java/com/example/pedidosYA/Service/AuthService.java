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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    @Autowired
    EmailService emailService;

    @Autowired
    UsuarioRepository usuarioRepository;

    private static final DateTimeFormatter HORA_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

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
                clienteValidations.validarEmail(request.getEmail());

                cliente.setUsuario(request.getUsuario());
                cliente.setContrasenia(passwordEncoder.encode(request.getContrasenia()));
                cliente.setNombreYapellido(request.getNombreYapellido());
                cliente.setEmail(request.getEmail());
                cliente.setRol(RolUsuario.CLIENTE);
                nuevoUsuario = cliente;
                break;

            case "RESTAURANTE":
                Restaurante restaurante = new Restaurante();

                restauranteValidations.validarNombreCrear(request.getNombreRestaurante());
                restauranteValidations.validarUsuario(request.getUsuario());
                restauranteValidations.validarContrasenia(request.getContrasenia());
                restauranteValidations.validarEmail(request.getEmail());

                if (request.getHoraApertura() == null || request.getHoraApertura().isBlank()
                        || request.getHoraCierre() == null || request.getHoraCierre().isBlank()) {
                    throw new RuntimeException("La hora de apertura y cierre son obligatorias para restaurantes");
                }

                LocalTime horaApertura = parseHora(request.getHoraApertura());
                LocalTime horaCierre = parseHora(request.getHoraCierre());

                restaurante.setUsuario(request.getUsuario());
                restaurante.setContrasenia(passwordEncoder.encode(request.getContrasenia()));
                restaurante.setNombre(request.getNombreRestaurante());
                restaurante.setEmail(request.getEmail());
                restaurante.setRol(RolUsuario.RESTAURANTE);
                restaurante.setHoraApertura(horaApertura);
                restaurante.setHoraCierre(horaCierre);

                nuevoUsuario = restaurante;
                break;

            default:
                throw new RuntimeException("Solo se pueden registrar usuarios con rol CLIENTE o RESTAURANTE.");
        }

        usuarioService.save(nuevoUsuario);

        // Si es un restaurante, notificar al admin
        if (nuevoUsuario instanceof Restaurante) {
            try {
                Usuario admin = usuarioRepository.findByRol(RolUsuario.ADMIN);
                if (admin != null && admin.getEmail() != null) {
                    Restaurante rest = (Restaurante) nuevoUsuario;
                    emailService.enviarEmailNuevaSolicitudAdmin(admin.getEmail(), rest.getNombre(), rest.getId());
                }
            } catch (Exception e) {
                // No interrumpir el flujo si falla el email
            }
        }

        return nuevoUsuario.getRol().name() + " creado";
    }

    private LocalTime parseHora(String value) {
        try {
            return LocalTime.parse(value, HORA_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("El horario debe tener formato HH:mm (por ejemplo, 09:00)", e);
        }
    }


    public String registrarAdmin(RegisterRequest request) {
        if (usuarioService.existsByRol(RolUsuario.ADMIN)) {
            return "Ya existe un administrador registrado.";
        }

        Admin admin = new Admin();

        adminValidations.validarUsuario(request.getUsuario());
        adminValidations.validarContrasenia(request.getContrasenia());
        adminValidations.validarEmail(request.getEmail());

        admin.setUsuario(request.getUsuario());
        admin.setContrasenia(passwordEncoder.encode(request.getContrasenia()));
        admin.setEmail(request.getEmail());
        admin.setRol(RolUsuario.ADMIN);

        usuarioService.save(admin);
        return "ADMIN creado";
    }
}
