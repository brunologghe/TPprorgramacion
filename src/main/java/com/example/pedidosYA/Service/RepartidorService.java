package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.PedidoDTO.PedidoRepartidorDTO;
import com.example.pedidosYA.DTO.RepartidorDTO.ActualizarPerfilRepartidorDTO;
import com.example.pedidosYA.DTO.RepartidorDTO.CambiarContraseniaRepartidorDTO;
import com.example.pedidosYA.DTO.RepartidorDTO.RepartidorDetailDTO;
import com.example.pedidosYA.DTO.RepartidorDTO.RepartidorResumenDTO;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.EstadoPedido;
import com.example.pedidosYA.Model.Pedido;
import com.example.pedidosYA.Model.Repartidor;
import com.example.pedidosYA.Repository.PedidoRepository;
import com.example.pedidosYA.Repository.RepartidorRepository;
import com.example.pedidosYA.Validations.RepartidorValidations;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepartidorService {

    @Autowired
    private RepartidorRepository repartidorRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private RepartidorValidations repartidorValidations;
    @Autowired
    PasswordEncoder passwordEncoder;
    public RepartidorDetailDTO obtenerPerfilRepartidor(String usuario) {
        Repartidor repartidor = repartidorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("Repartidor no encontrado"));

        if (repartidor.getActivo() != null && !repartidor.getActivo()) {
            throw new BusinessException("El repartidor no está activo en el sistema.");
        }

        return new RepartidorDetailDTO(
                repartidor.getId(),
                repartidor.getUsuario(),
                repartidor.getNombreYapellido(),
                repartidor.getEmail(),
                repartidor.getPais(),
                repartidor.getTipoVehiculo(),
                repartidor.getDisponible(),
                repartidor.getTrabajando(),
                repartidor.getTotalPedidosEntregados(),
                repartidor.getCalificacionPromedio(),
                repartidor.getActivo(),
                repartidor.getZonas()
        );
    }

    @Transactional
    public RepartidorDetailDTO actualizarPerfil(String usuario, ActualizarPerfilRepartidorDTO dto) {
        Repartidor repartidor = repartidorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("Repartidor no encontrado"));
        repartidorValidations.validarContraseniaActual(repartidor.getId(), dto.getContraseniaActual());

        if (dto.getNombreYapellido() != null) {
            repartidorValidations.validarNombreYApellido(dto.getNombreYapellido());
            repartidor.setNombreYapellido(dto.getNombreYapellido());
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(repartidor.getEmail())) {
            repartidorValidations.validarEmailModificacion(repartidor.getId(), dto.getEmail());
            repartidor.setEmail(dto.getEmail());
        }

        if (dto.getPais() != null) {
            repartidor.setPais(dto.getPais());
        }

        if (dto.getTipoVehiculo() != null) {
            repartidor.setTipoVehiculo(dto.getTipoVehiculo());
        }

        Repartidor updated = repartidorRepository.save(repartidor);

        return new RepartidorDetailDTO(
                updated.getId(),
                updated.getUsuario(),
                updated.getNombreYapellido(),
                updated.getEmail(),
                updated.getPais(),
                updated.getTipoVehiculo(),
                updated.getDisponible(),
                updated.getTrabajando(),
                updated.getTotalPedidosEntregados(),
                updated.getCalificacionPromedio(),
                updated.getActivo(),
                updated.getZonas()
        );
    }

    @Transactional
    public void cambiarContrasenia(String usuario, CambiarContraseniaRepartidorDTO dto) {
        Repartidor repartidor = repartidorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("Repartidor no encontrado"));
        repartidorValidations.validarContraseniaActual(repartidor.getId(), dto.getContraseniaActual());

        if (!dto.getContraseniaNueva().equals(dto.getConfirmarContrasenia())) {
            throw new BusinessException("Las contraseñas no coinciden.");
        }

        repartidorValidations.validarContrasenia(dto.getContraseniaNueva());

        repartidor.setContrasenia(passwordEncoder.encode(dto.getContraseniaNueva()));
        repartidorRepository.save(repartidor);
    }

    @Transactional
    public void cambiarDisponibilidad(String usuario, Boolean disponible) {
        System.out.println("🔄 [RepartidorService] Cambiando disponibilidad");
        System.out.println("👤 Usuario: " + usuario);
        System.out.println("📍 Nuevo estado disponible: " + disponible);
        
        Repartidor repartidor = repartidorRepository.findByUsuario(usuario)
                .orElseThrow(() -> {
                    System.out.println("❌ Repartidor no encontrado");
                    return new BusinessException("Repartidor no encontrado");
                });

        System.out.println("✅ Repartidor encontrado: " + repartidor.getNombreYapellido());
        System.out.println("📊 Estado anterior disponible: " + repartidor.getDisponible());

        if (!disponible && repartidor.getPedidoActual() != null) {
            System.out.println("❌ No puede ponerse no disponible con pedido en curso");
            throw new BusinessException("No puede ponerse no disponible mientras tenga un pedido en curso.");
        }

        repartidor.setDisponible(disponible);
        repartidorRepository.save(repartidor);
        
        System.out.println("✅ Disponibilidad actualizada a: " + disponible);
    }

    public List<PedidoRepartidorDTO> obtenerPedidosDisponibles(String usuario) {
        // Validar que el repartidor existe
        repartidorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("Repartidor no encontrado"));

        // Obtener pedidos en estado PREPARACION sin repartidor asignado
        List<Pedido> pedidosDisponibles = pedidoRepository.findByEstado(EstadoPedido.PREPARACION);

        return pedidosDisponibles.stream()
                .map(this::convertirAPedidoRepartidorDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void tomarPedido(String usuario, Long pedidoId) {
        Repartidor repartidor = repartidorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("Repartidor no encontrado"));
        
        repartidorValidations.validarDisponible(repartidor.getId());
        repartidorValidations.validarPedidoDisponible(pedidoId);
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new BusinessException("Pedido no encontrado"));

        // Asignar pedido
        pedido.setEstado(EstadoPedido.ENVIADO);
        pedido.setRepartidor(repartidor);
        repartidor.setPedidoActual(pedido);
        repartidor.setDisponible(false);  // Ya no está disponible, tiene un pedido

        pedidoRepository.save(pedido);
        repartidorRepository.save(repartidor);
    }

    public PedidoRepartidorDTO obtenerPedidoActual(String usuario) {
        Repartidor repartidor = repartidorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("Repartidor no encontrado"));

        if (repartidor.getPedidoActual() == null) {
            throw new BusinessException("El repartidor no tiene ningún pedido asignado actualmente.");
        }

        Pedido pedido = repartidor.getPedidoActual();
        
        return convertirAPedidoRepartidorDTO(pedido);
    }

    @Transactional
    public void marcarComoEntregado(String usuario, Long pedidoId) {
        Repartidor repartidor = repartidorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("Repartidor no encontrado"));
        
        repartidorValidations.validarPedidoAsignadoARepartidor(pedidoId, repartidor.getId());
        repartidorValidations.validarPedidoEnviado(pedidoId);
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new BusinessException("Pedido no encontrado"));

        // Marcar como entregado
        pedido.setEstado(EstadoPedido.ENTREGADO);
        repartidor.setPedidoActual(null);
        repartidor.setDisponible(true);  // Vuelve a estar disponible
        repartidor.setTotalPedidosEntregados(repartidor.getTotalPedidosEntregados() + 1);

        pedidoRepository.save(pedido);
        repartidorRepository.save(repartidor);
    }

    public List<PedidoRepartidorDTO> obtenerHistorialEntregas(String usuario) {
        // Validar que el repartidor existe
        repartidorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("Repartidor no encontrado"));

        // Por ahora retorna todos los pedidos ENTREGADO
        // Luego se puede agregar filtro por repartidor cuando se agregue la relación en Pedido
        List<Pedido> historial = pedidoRepository.findByEstado(EstadoPedido.ENTREGADO);
        
        return historial.stream()
                .map(this::convertirAPedidoRepartidorDTO)
                .collect(Collectors.toList());
    }

    public RepartidorDetailDTO obtenerEstadisticas(String usuario) {
        Repartidor repartidor = repartidorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("Repartidor no encontrado"));

        // Las estadísticas ya están en el modelo
        return new RepartidorDetailDTO(
                repartidor.getId(),
                repartidor.getUsuario(),
                repartidor.getNombreYapellido(),
                repartidor.getEmail(),
                repartidor.getPais(),
                repartidor.getTipoVehiculo(),
                repartidor.getDisponible(),
                repartidor.getTrabajando(),
                repartidor.getTotalPedidosEntregados(),
                repartidor.getCalificacionPromedio(),
                repartidor.getActivo(),
                repartidor.getZonas()
        );
    }

    public List<RepartidorResumenDTO> listarTodos() {
        return repartidorRepository.findAll().stream()
                .filter(r -> r.getActivo() != null && r.getActivo())
                .map(r -> new RepartidorResumenDTO(
                        r.getId(),
                        r.getNombreYapellido(),
                        r.getEmail(),
                        r.getPais(),
                        r.getTipoVehiculo()
                ))
                .collect(Collectors.toList());
    }

    // Métodos para Admin
    public RepartidorDetailDTO obtenerRepartidorPorIdAdmin(Long id) {
        Repartidor repartidor = repartidorValidations.validarExistencia(id);

        return new RepartidorDetailDTO(
                repartidor.getId(),
                repartidor.getUsuario(),
                repartidor.getNombreYapellido(),
                repartidor.getEmail(),
                repartidor.getPais(),
                repartidor.getTipoVehiculo(),
                repartidor.getDisponible(),
                repartidor.getTrabajando(),
                repartidor.getTotalPedidosEntregados(),
                repartidor.getCalificacionPromedio(),
                repartidor.getActivo(),
                repartidor.getZonas()
        );
    }

    @Transactional
    public RepartidorResumenDTO eliminarRepartidor(Long id) {
        Repartidor repartidor = repartidorValidations.validarExistencia(id);

        if (repartidor.getPedidoActual() != null) {
            throw new BusinessException("No se puede eliminar un repartidor con pedido en curso.");
        }

        RepartidorResumenDTO resumen = new RepartidorResumenDTO(
                repartidor.getId(),
                repartidor.getNombreYapellido(),
                repartidor.getEmail(),
                repartidor.getPais(),
                repartidor.getTipoVehiculo()
        );

        // Eliminación pasiva
        repartidor.setActivo(false);
        repartidor.setDisponible(false);
        repartidor.setTrabajando(false);
        repartidorRepository.save(repartidor);

        return resumen;
    }

    // Método privado para convertir Pedido a DTO (evita recursión infinita)
    private PedidoRepartidorDTO convertirAPedidoRepartidorDTO(Pedido pedido) {
        String direccionRestaurante = "";
        String direccionEntrega = "";
        String codigoPostal = "";
        
        if (pedido.getDireccionRestaurante() != null) {
            direccionRestaurante = pedido.getDireccionRestaurante().getDireccion();
        }
        
        if (pedido.getDireccionEntrega() != null) {
            direccionEntrega = pedido.getDireccionEntrega().getDireccion();
            codigoPostal = pedido.getDireccionEntrega().getCodigoPostal();
        }
        
        return new PedidoRepartidorDTO(
                pedido.getId(),
                pedido.getFechaPedido(),
                pedido.getTotal(),
                pedido.getEstado(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNombreYapellido(),
                pedido.getCliente().getEmail(),
                pedido.getRestaurante().getId(),
                pedido.getRestaurante().getNombre(),
                direccionRestaurante,
                direccionEntrega,
                codigoPostal
        );
    }

    // Método para activar cuenta sin validar estado activo previo
    public void activarCuenta(String usuario) {
        Repartidor repartidor = repartidorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("Repartidor no encontrado"));
        
        repartidor.setActivo(true);
        repartidorRepository.save(repartidor);
    }
}