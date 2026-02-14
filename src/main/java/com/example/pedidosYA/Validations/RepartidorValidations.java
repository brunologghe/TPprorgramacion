package com.example.pedidosYA.Validations;

import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.EstadoPedido;
import com.example.pedidosYA.Model.Pedido;
import com.example.pedidosYA.Model.Repartidor;
import com.example.pedidosYA.Model.TipoVehiculo;
import com.example.pedidosYA.Model.Usuario;
import com.example.pedidosYA.Repository.PedidoRepository;
import com.example.pedidosYA.Repository.RepartidorRepository;
import com.example.pedidosYA.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class RepartidorValidations {

    @Autowired
    private RepartidorRepository repartidorRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Repartidor validarExistencia(Long id) {
        Repartidor repartidor = repartidorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("No existe ningún repartidor con ese id"));
        
        if (repartidor.getActivo() != null && !repartidor.getActivo()) {
            throw new BusinessException("El repartidor no está activo en el sistema.");
        }
        
        return repartidor;
    }

    public void validarContraseniaActual(Long id, String contrasenia) {
        Repartidor repartidor = repartidorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Repartidor no encontrado con id: " + id));

        if (!passwordEncoder.matches(contrasenia, repartidor.getContrasenia())) {
            throw new BusinessException("La contraseña actual es incorrecta.");
        }
    }

    public void validarUsuario(String usuario) {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new BusinessException("El usuario es obligatorio.");
        }
        if (!usuario.matches("^[a-zA-Z0-9]{3,18}$")) {
            throw new BusinessException("El usuario debe tener entre 3 y 18 caracteres y solo puede contener letras o números.");
        }
        if (usuarioRepository.existsByUsuario(usuario)) {
            throw new BusinessException("El usuario ya existe en el sistema.");
        }
    }

    public void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("El email es obligatorio.");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BusinessException("El formato del email no es válido.");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new BusinessException("El email ya está registrado en el sistema.");
        }
    }

    public void validarEmailModificacion(Long id, String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("El email es obligatorio.");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BusinessException("El formato del email no es válido.");
        }
        Usuario usuarioConEmail = usuarioRepository.findByEmail(email).orElse(null);
        if (usuarioConEmail != null && !usuarioConEmail.getId().equals(id)) {
            throw new BusinessException("El email ya está registrado en el sistema.");
        }
    }

    public void validarDisponible(Long repartidorId) {
        Repartidor repartidor = validarExistencia(repartidorId);
        if (repartidor.getDisponible() == null || !repartidor.getDisponible()) {
            throw new BusinessException("El repartidor no está disponible para tomar pedidos.");
        }
    }

    public void validarNoTienePedidoEnCurso(Long repartidorId) {
        Repartidor repartidor = validarExistencia(repartidorId);
        if (repartidor.getPedidoActual() != null) {
            throw new BusinessException("El repartidor ya tiene un pedido en curso. Debe entregarlo antes de tomar otro.");
        }
    }

    public void validarPedidoDisponible(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new BusinessException("No existe ningún pedido con ese id"));

        if (pedido.getEstado() != EstadoPedido.PREPARACION) {
            throw new BusinessException("El pedido no está disponible para ser tomado. Estado actual: " + pedido.getEstado());
        }
    }

    public void validarPedidoAsignadoARepartidor(Long pedidoId, Long repartidorId) {
        Repartidor repartidor = validarExistencia(repartidorId);
        if (repartidor.getPedidoActual() == null || !repartidor.getPedidoActual().getId().equals(pedidoId)) {
            throw new BusinessException("Este pedido no está asignado a este repartidor.");
        }
    }

    public void validarPedidoEnviado(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new BusinessException("No existe ningún pedido con ese id"));

        if (pedido.getEstado() != EstadoPedido.ENVIADO) {
            throw new BusinessException("El pedido no puede ser marcado como entregado. Estado actual: " + pedido.getEstado());
        }
    }

    public void validarNombreYApellido(String nombreYapellido) {
        if (nombreYapellido == null || nombreYapellido.trim().isEmpty()) {
            throw new BusinessException("El nombre y apellido es obligatorio.");
        }
        if (nombreYapellido.length() < 3 || nombreYapellido.length() > 100) {
            throw new BusinessException("El nombre y apellido debe tener entre 3 y 100 caracteres.");
        }
    }

    public void validarContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.trim().isEmpty()) {
            throw new BusinessException("La contraseña es obligatoria.");
        }
        if (contrasenia.length() < 6 || contrasenia.length() > 100) {
            throw new BusinessException("La contraseña debe tener entre 6 y 100 caracteres.");
        }
    }

    public void validarTipoVehiculo(TipoVehiculo tipoVehiculo) {
        if (tipoVehiculo == null) {
            throw new BusinessException("El tipo de vehículo es obligatorio.");
        }
    }

    public void validarPais(String pais) {
        if (pais == null || pais.trim().isEmpty()) {
            throw new BusinessException("El país es obligatorio.");
        }
    }
}
