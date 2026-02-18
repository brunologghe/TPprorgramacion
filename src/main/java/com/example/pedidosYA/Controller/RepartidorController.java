package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.PedidoDTO.PedidoRepartidorDTO;
import com.example.pedidosYA.DTO.RepartidorDTO.ActualizarPerfilRepartidorDTO;
import com.example.pedidosYA.DTO.RepartidorDTO.CambiarContraseniaRepartidorDTO;
import com.example.pedidosYA.DTO.RepartidorDTO.RepartidorDetailDTO;
import com.example.pedidosYA.Model.Pedido;
import com.example.pedidosYA.Security.AuthUtil;
import com.example.pedidosYA.Service.RepartidorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repartidores")
public class RepartidorController {

    @Autowired
    private RepartidorService repartidorService;

    @GetMapping("/perfil")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<RepartidorDetailDTO> verPerfil() {
        return ResponseEntity.ok(repartidorService.obtenerPerfilRepartidor(AuthUtil.getUsuarioLogueado()));
    }

    @PutMapping("/perfil")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<?> actualizarPerfil(@Valid @RequestBody ActualizarPerfilRepartidorDTO perfilDTO) {
        repartidorService.actualizarPerfil(AuthUtil.getUsuarioLogueado(), perfilDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Perfil actualizado con éxito!");
    }

    @PutMapping("/contrasenia")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<?> cambiarContrasenia(@Valid @RequestBody CambiarContraseniaRepartidorDTO contraseniaDTO) {
        repartidorService.cambiarContrasenia(AuthUtil.getUsuarioLogueado(), contraseniaDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Contraseña cambiada con éxito!");
    }

    @PutMapping("/disponibilidad")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<?> cambiarDisponibilidad(@RequestParam Boolean trabajando) {
        repartidorService.cambiarDisponibilidad(AuthUtil.getUsuarioLogueado(), trabajando);
        return ResponseEntity.status(HttpStatus.OK).body("Estado de turno actualizado!");
    }

    @GetMapping("/pedidos-disponibles")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<List<PedidoRepartidorDTO>> obtenerPedidosDisponibles() {
        return ResponseEntity.ok(repartidorService.obtenerPedidosDisponibles(AuthUtil.getUsuarioLogueado()));
    }

    @PostMapping("/pedidos/{id}/tomar")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<?> tomarPedido(@PathVariable("id") Long pedidoId) {
        repartidorService.tomarPedido(AuthUtil.getUsuarioLogueado(), pedidoId);
        return ResponseEntity.status(HttpStatus.OK).body("Pedido asignado exitosamente!");
    }

    @GetMapping("/pedido-actual")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<PedidoRepartidorDTO> obtenerPedidoActual() {
        return ResponseEntity.ok(repartidorService.obtenerPedidoActual(AuthUtil.getUsuarioLogueado()));
    }

    @PostMapping("/pedidos/{id}/entregar")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<?> marcarComoEntregado(@PathVariable("id") Long pedidoId) {
        repartidorService.marcarComoEntregado(AuthUtil.getUsuarioLogueado(), pedidoId);
        return ResponseEntity.status(HttpStatus.OK).body("Pedido marcado como entregado!");
    }

    @GetMapping("/historial")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<List<PedidoRepartidorDTO>> obtenerHistorial() {
        return ResponseEntity.ok(repartidorService.obtenerHistorialEntregas(AuthUtil.getUsuarioLogueado()));
    }

    @GetMapping("/estadisticas")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<RepartidorDetailDTO> obtenerEstadisticas() {
        return ResponseEntity.ok(repartidorService.obtenerEstadisticas(AuthUtil.getUsuarioLogueado()));
    }

    @PutMapping("/activar")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<?> activarCuenta() {
        repartidorService.activarCuenta(AuthUtil.getUsuarioLogueado());
        return ResponseEntity.status(HttpStatus.OK).body("Cuenta activada exitosamente!");
    }
}
