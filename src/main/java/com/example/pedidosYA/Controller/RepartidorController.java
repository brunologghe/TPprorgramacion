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
            System.out.println("✅ [RepartidorController] Actualizando perfil");
            System.out.println("👤 Usuario: " + usuario);

            RepartidorDetailDTO perfilActualizado = repartidorService.actualizarPerfil(usuario, perfilDTO);

            System.out.println("✅ Perfil actualizado exitosamente");
            return ResponseEntity.status(HttpStatus.OK).body(perfilActualizado);
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseBuilder.error(e.getMessage()));
        }
    }

    @PutMapping("/contrasenia")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<?> cambiarContrasenia(@Valid @RequestBody CambiarContraseniaRepartidorDTO contraseniaDTO) {
        try {
            String usuario = AuthUtil.getUsuarioLogueado();
            System.out.println("✅ [RepartidorController] Cambiando contraseña");
            System.out.println("👤 Usuario: " + usuario);

            repartidorService.cambiarContrasenia(usuario, contraseniaDTO);

            System.out.println("✅ Contraseña cambiada exitosamente");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseBuilder.success("Contraseña cambiada con éxito!"));
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseBuilder.error(e.getMessage()));
        }
    }

    @PutMapping("/disponibilidad")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<?> cambiarDisponibilidad(@RequestParam("disponible") Boolean disponible) {
        try {
            String usuario = AuthUtil.getUsuarioLogueado();
            System.out.println("✅ [RepartidorController] Cambiando disponibilidad");
            System.out.println("👤 Usuario: " + usuario);
            System.out.println("📍 Disponible: " + disponible);

            repartidorService.cambiarDisponibilidad(usuario, disponible);

            System.out.println("✅ Disponibilidad cambiada exitosamente");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseBuilder.successWithProperty("Estado de disponibilidad actualizado!", "disponible", disponible));
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
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
            System.out.println("✅ [RepartidorController] Tomando pedido");
            System.out.println("👤 Usuario: " + usuario);
            System.out.println("📦 Pedido ID: " + pedidoId);

            repartidorService.tomarPedido(usuario, pedidoId);

            System.out.println("✅ Pedido asignado exitosamente");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseBuilder.success("Pedido asignado exitosamente!"));
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
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
            System.out.println("✅ [RepartidorController] Marcando pedido como entregado");
            System.out.println("👤 Usuario: " + usuario);
            System.out.println("📦 Pedido ID: " + pedidoId);

            repartidorService.marcarComoEntregado(usuario, pedidoId);

            System.out.println("✅ Pedido marcado como entregado exitosamente");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseBuilder.success("Pedido marcado como entregado!"));
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
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
            
            System.out.println("✅ [RepartidorController] Cambiando estado del pedido");
            System.out.println("👤 Usuario: " + usuario);
            System.out.println("📦 Pedido ID: " + pedidoId);
            System.out.println("🔄 Nuevo estado: " + estado);

            repartidorService.cambiarEstadoPedido(usuario, pedidoId, estado);

            System.out.println("✅ Estado del pedido actualizado");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseBuilder.success("Estado del pedido actualizado a: " + estado));
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseBuilder.error(e.getMessage()));
        }
    }

    @PutMapping("/activar")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<?> activarCuenta() {
        try {
            String usuario = AuthUtil.getUsuarioLogueado();
            System.out.println("✅ [RepartidorController] Activando cuenta");
            System.out.println("👤 Usuario: " + usuario);

            repartidorService.activarCuenta(usuario);

            System.out.println("✅ Cuenta activada exitosamente");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseBuilder.success("Cuenta activada exitosamente!"));
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseBuilder.error(e.getMessage()));
        }
    }

    @PutMapping("/desactivar")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public ResponseEntity<?> desactivarDisponibilidad() {
        try {
            String usuario = AuthUtil.getUsuarioLogueado();
            System.out.println("✅ [RepartidorController] Desactivando disponibilidad");
            System.out.println("👤 Usuario: " + usuario);

            repartidorService.cambiarDisponibilidad(usuario, false);

            System.out.println("✅ Disponibilidad desactivada exitosamente");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseBuilder.successWithProperty("Estado de disponibilidad actualizado!", "disponible", false));
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseBuilder.error(e.getMessage()));
        }
    }
}
