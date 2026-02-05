package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.RepartidorDTO.*;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.EstadoPedido;
import com.example.pedidosYA.Model.Pedido;
import com.example.pedidosYA.Model.Repartidor;
import com.example.pedidosYA.Model.RolUsuario;
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
    private PasswordEncoder passwordEncoder;

    @Transactional
    public RepartidorDetailDTO crearRepartidor(RepartidorCrearDTO dto) {
        repartidorValidations.validarUsuario(dto.getUsuario());
        repartidorValidations.validarEmail(dto.getEmail());
        repartidorValidations.validarNombreYApellido(dto.getNombreYapellido());
        repartidorValidations.validarContrasenia(dto.getContrasenia());
        repartidorValidations.validarPais(dto.getPais());

        Repartidor repartidor = new Repartidor();
        repartidor.setUsuario(dto.getUsuario());
        repartidor.setContrasenia(passwordEncoder.encode(dto.getContrasenia()));
        repartidor.setEmail(dto.getEmail());
        repartidor.setNombreYapellido(dto.getNombreYapellido());
        repartidor.setPais(dto.getPais());
        repartidor.setTipoVehiculo(dto.getTipoVehiculo());
        repartidor.setRol(RolUsuario.REPARTIDOR);
        repartidor.setDisponible(false);
        repartidor.setTrabajando(false);
        repartidor.setTotalPedidosEntregados(0);
        repartidor.setCalificacionPromedio(0.0);

        Repartidor saved = repartidorRepository.save(repartidor);

        return new RepartidorDetailDTO(
                saved.getId(),
                saved.getUsuario(),
                saved.getNombreYapellido(),
                saved.getEmail(),
                saved.getPais(),
                saved.getTipoVehiculo(),
                saved.getDisponible(),
                saved.getTrabajando(),
                saved.getTotalPedidosEntregados(),
                saved.getCalificacionPromedio()
        );
    }

    public RepartidorDetailDTO obtenerPerfilRepartidor(Long id) {
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
                repartidor.getCalificacionPromedio()
        );
    }

    @Transactional
    public RepartidorDetailDTO actualizarPerfil(Long id, ActualizarPerfilRepartidorDTO dto) {
        Repartidor repartidor = repartidorValidations.validarExistencia(id);
        repartidorValidations.validarContraseniaActual(id, dto.getContraseniaActual());

        if (dto.getNombreYapellido() != null) {
            repartidorValidations.validarNombreYApellido(dto.getNombreYapellido());
            repartidor.setNombreYapellido(dto.getNombreYapellido());
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(repartidor.getEmail())) {
            repartidorValidations.validarEmail(dto.getEmail());
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
                updated.getCalificacionPromedio()
        );
    }

    @Transactional
    public void cambiarContrasenia(Long id, CambiarContraseniaRepartidorDTO dto) {
        Repartidor repartidor = repartidorValidations.validarExistencia(id);
        repartidorValidations.validarContraseniaActual(id, dto.getContraseniaActual());

        if (!dto.getContraseniaNueva().equals(dto.getConfirmarContrasenia())) {
            throw new BusinessException("Las contraseñas no coinciden.");
        }

        repartidorValidations.validarContrasenia(dto.getContraseniaNueva());

        repartidor.setContrasenia(passwordEncoder.encode(dto.getContraseniaNueva()));
        repartidorRepository.save(repartidor);
    }

    @Transactional
    public void cambiarDisponibilidad(Long id, Boolean disponible) {
        Repartidor repartidor = repartidorValidations.validarExistencia(id);

        if (disponible && repartidor.getPedidoActualId() != null) {
            throw new BusinessException("No puede ponerse disponible mientras tenga un pedido en curso.");
        }

        repartidor.setDisponible(disponible);
        repartidorRepository.save(repartidor);
    }

    public List<Pedido> obtenerPedidosDisponibles(Long repartidorId) {
        Repartidor repartidor = repartidorValidations.validarExistencia(repartidorId);

        // Obtener pedidos en estado PREPARACION sin repartidor asignado
        List<Pedido> pedidosDisponibles = pedidoRepository.findByEstado(EstadoPedido.PREPARACION);

        // Filtrar por zona si el repartidor tiene zonas configuradas
        // (Por ahora retorna todos, la lógica de zonas se puede agregar después)

        return pedidosDisponibles;
    }

    @Transactional
    public void tomarPedido(Long repartidorId, Long pedidoId) {
        repartidorValidations.validarDisponible(repartidorId);
        repartidorValidations.validarNoTienePedidoEnCurso(repartidorId);
        repartidorValidations.validarPedidoDisponible(pedidoId);

        Repartidor repartidor = repartidorValidations.validarExistencia(repartidorId);
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new BusinessException("Pedido no encontrado"));

        // Asignar pedido
        pedido.setEstado(EstadoPedido.ENVIADO);
        repartidor.setPedidoActualId(pedidoId);
        repartidor.setTrabajando(true);

        pedidoRepository.save(pedido);
        repartidorRepository.save(repartidor);
    }

    public Pedido obtenerPedidoActual(Long repartidorId) {
        Repartidor repartidor = repartidorValidations.validarExistencia(repartidorId);

        if (repartidor.getPedidoActualId() == null) {
            throw new BusinessException("El repartidor no tiene ningún pedido asignado actualmente.");
        }

        return pedidoRepository.findById(repartidor.getPedidoActualId())
                .orElseThrow(() -> new BusinessException("Pedido actual no encontrado"));
    }

    @Transactional
    public void marcarComoEntregado(Long repartidorId, Long pedidoId) {
        repartidorValidations.validarPedidoAsignadoARepartidor(pedidoId, repartidorId);
        repartidorValidations.validarPedidoEnviado(pedidoId);

        Repartidor repartidor = repartidorValidations.validarExistencia(repartidorId);
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new BusinessException("Pedido no encontrado"));

        // Marcar como entregado
        pedido.setEstado(EstadoPedido.ENTREGADO);
        repartidor.setPedidoActualId(null);
        repartidor.setTrabajando(false);
        repartidor.setTotalPedidosEntregados(repartidor.getTotalPedidosEntregados() + 1);

        pedidoRepository.save(pedido);
        repartidorRepository.save(repartidor);
    }

    public List<Pedido> obtenerHistorialEntregas(Long repartidorId) {
        repartidorValidations.validarExistencia(repartidorId);

        // Por ahora retorna todos los pedidos ENTREGADO
        // Luego se puede agregar filtro por repartidor cuando se agregue la relación en Pedido
        return pedidoRepository.findByEstado(EstadoPedido.ENTREGADO);
    }

    public RepartidorDetailDTO obtenerEstadisticas(Long repartidorId) {
        Repartidor repartidor = repartidorValidations.validarExistencia(repartidorId);

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
                repartidor.getCalificacionPromedio()
        );
    }

    public List<RepartidorResumenDTO> listarTodos() {
        return repartidorRepository.findAll().stream()
                .map(r -> new RepartidorResumenDTO(
                        r.getId(),
                        r.getNombreYapellido(),
                        r.getEmail(),
                        r.getPais(),
                        r.getTipoVehiculo()
                ))
                .collect(Collectors.toList());
    }
}