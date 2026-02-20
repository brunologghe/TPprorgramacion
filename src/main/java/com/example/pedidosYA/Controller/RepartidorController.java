package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.PedidoDTO.PedidoRepartidorDTO;
import com.example.pedidosYA.DTO.RepartidorDTO.ActualizarPerfilRepartidorDTO;
import com.example.pedidosYA.DTO.RepartidorDTO.CambiarContraseniaRepartidorDTO;
import com.example.pedidosYA.DTO.RepartidorDTO.RepartidorDetailDTO;
import com.example.pedidosYA.Security.AuthUtil;
import com.example.pedidosYA.Service.RepartidorService;
import com.example.pedidosYA.Utils.ResponseBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        try {
            String usuario = AuthUtil.getUsuarioLogueado();
            RepartidorDetailDTO perfilActualizado = repartidorService.actualizarPerfil(usuario, perfilDTO);
            return ResponseEntity.status(HttpStatus.OK).body(perfilActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseBuilder.error(e.getMessage()));
        }
    }

    @PutMapping("/contrasenia")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<?> cambiarContrasenia(@Valid @RequestBody CambiarContraseniaRepartidorDTO contraseniaDTO) {
        try {
            String usuario = AuthUtil.getUsuarioLogueado();
            repartidorService.cambiarContrasenia(usuario, contraseniaDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseBuilder.success("Contraseña cambiada con éxito!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseBuilder.error(e.getMessage()));
        }
    }

    @PutMapping("/disponibilidad")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<?> cambiarDisponibilidad(@RequestParam("disponible") Boolean disponible) {
        try {
            String usuario = AuthUtil.getUsuarioLogueado();
            repartidorService.cambiarDisponibilidad(usuario, disponible);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseBuilder.successWithProperty("Estado de disponibilidad actualizado!", "disponible", disponible));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseBuilder.error(e.getMessage()));
        }
    }

    @GetMapping("/pedidos-disponibles")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<List<PedidoRepartidorDTO>> obtenerPedidosDisponibles() {
        return ResponseEntity.ok(repartidorService.obtenerPedidosDisponibles(AuthUtil.getUsuarioLogueado()));
    }

    @PostMapping("/pedidos/{id}/tomar")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<?> tomarPedido(@PathVariable("id") Long pedidoId) {
        try {
            String usuario = AuthUtil.getUsuarioLogueado();
            repartidorService.tomarPedido(usuario, pedidoId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseBuilder.success("Pedido asignado exitosamente!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseBuilder.error(e.getMessage()));
        }
    }

    @GetMapping("/pedido-actual")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<PedidoRepartidorDTO> obtenerPedidoActual() {
        return ResponseEntity.ok(repartidorService.obtenerPedidoActual(AuthUtil.getUsuarioLogueado()));
    }

    @PostMapping("/pedidos/{id}/entregar")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<?> marcarComoEntregado(@PathVariable("id") Long pedidoId) {
        try {
            String usuario = AuthUtil.getUsuarioLogueado();
            repartidorService.marcarComoEntregado(usuario, pedidoId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseBuilder.success("Pedido marcado como entregado!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseBuilder.error(e.getMessage()));
        }
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

    @PutMapping("/pedidos/{id}/estado")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<?> cambiarEstadoPedido(@PathVariable("id") Long pedidoId, @RequestBody Map<String, String> requestBody) {
        try {
            String usuario = AuthUtil.getUsuarioLogueado();
            String estado = requestBody.get("estado");
            repartidorService.cambiarEstadoPedido(usuario, pedidoId, estado);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseBuilder.success("Estado del pedido actualizado a: " + estado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseBuilder.error(e.getMessage()));
        }
    }

    @PutMapping("/activar")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<?> activarCuenta() {
        try {
            String usuario = AuthUtil.getUsuarioLogueado();
            repartidorService.activarCuenta(usuario);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseBuilder.success("Cuenta activada exitosamente!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseBuilder.error(e.getMessage()));
        }
    }

    @PutMapping("/desactivar")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<?> desactivarDisponibilidad() {
        try {
            String usuario = AuthUtil.getUsuarioLogueado();
            repartidorService.cambiarDisponibilidad(usuario, false);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseBuilder.successWithProperty("Estado de disponibilidad actualizado!", "disponible", false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseBuilder.error(e.getMessage()));
        }
    }
}
