
package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.ClienteDTO.ActualizarPerfilDTO;
import com.example.pedidosYA.DTO.ClienteDTO.CambiarContraseniaDTO;
import com.example.pedidosYA.DTO.ClienteDTO.ClienteDetailDto;
import com.example.pedidosYA.DTO.ClienteDTO.ModificarDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoCreateDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoDetailDTO;
import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaCreateDTO;
import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaDetailDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteDetailDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteResponseDTO;
import com.example.pedidosYA.Security.AuthUtil;
import com.example.pedidosYA.Service.*;
import com.example.pedidosYA.Utils.ResponseBuilder;
import jakarta.persistence.DiscriminatorValue;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/clientes")
@DiscriminatorValue("CLIENTE")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ReseniaService reseniaService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private RestauranteService restauranteService;


    @GetMapping("/perfil")
    @PreAuthorize("hasRole('CLIENTE')")
    public ClienteDetailDto verCliente() {
        return clienteService.verUsuarioPorNombre(AuthUtil.getUsuarioLogueado());
    }



    @PutMapping("/contrasenia")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> modificarContraseniaCliente (@Valid @RequestBody ModificarDTO modificarDTO){
        clienteService.modificarContrasenia(AuthUtil.getUsuarioLogueado(), modificarDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Contrasenia cambiada con exito!");
    }

    @PutMapping("/perfil")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> actualizarPerfil(@Valid @RequestBody ActualizarPerfilDTO perfilDTO) {
        clienteService.actualizarPerfil(AuthUtil.getUsuarioLogueado(), perfilDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Perfil actualizado con éxito!");
    }

    @PutMapping("/nueva-contrasenia")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> cambiarContrasenia(@Valid @RequestBody CambiarContraseniaDTO contraseniaDTO) {
        clienteService.cambiarContrasenia(AuthUtil.getUsuarioLogueado(), contraseniaDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Contraseña cambiada con éxito!");
    }

    @GetMapping("/restaurantes")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Set<RestauranteResponseDTO>> listAllRestaurantes(){
        return ResponseEntity.ok(restauranteService.findAllRestaurantes());
    }

    @GetMapping ("/ver-menu/{usuario}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> findALlProducto(@PathVariable String usuario){
        return ResponseEntity.ok(productoService.findAllProductosByRestaurante(usuario));
    }

    @GetMapping("/restaurante/{usuario}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<RestauranteDetailDTO> verRestauranteCompleto(@PathVariable String usuario) {
        return ResponseEntity.ok(restauranteService.findRestauranteByNombre(usuario));
    }

    @PostMapping("/pedir")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<PedidoDetailDTO> hacerPedido(@Valid @RequestBody PedidoCreateDTO pedido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.hacerPedido(AuthUtil.getUsuarioLogueado(), pedido));
    }

    @PostMapping("/resenias")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ReseniaDetailDTO> hacerResenia(@Valid @RequestBody ReseniaCreateDTO reseniaCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reseniaService.crearResenia(AuthUtil.getUsuarioLogueado(), reseniaCreateDTO));
    }

    @GetMapping("/pedidos-en-curso")
    @PreAuthorize("hasRole('CLIENTE')")
    public List<PedidoDetailDTO> verPedidosEnCurso() {
        return pedidoService.verPedidosEnCurso(AuthUtil.getUsuarioLogueado());
    }

    @GetMapping("/historial-pedidos")
    @PreAuthorize("hasRole('CLIENTE')")
    public List<PedidoDetailDTO> verHistorialPedidos() {
        return pedidoService.verHistorialPedidos(AuthUtil.getUsuarioLogueado());
    }

    @GetMapping("/ver-detalles-pedidos/{id-pedido}")
    @PreAuthorize("hasRole('CLIENTE')")
    public PedidoDetailDTO verDetallesPedido(@PathVariable("id-pedido") Long idPedido) {
        return pedidoService.verDetallesPedido(idPedido);
    }

    @PutMapping("/pedido/{id}/cancelar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> cancelarPedido(@PathVariable("id") Long idPedido) {
        pedidoService.cancelarPedido(AuthUtil.getUsuarioLogueado(), idPedido);
        return ResponseEntity.status(HttpStatus.OK).body("Pedido cancelado exitosamente");
    }

    @PostMapping("/agregar-listafav/{id-restaurante}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> agregarRestauranteALista(@PathVariable("id-restaurante") Long idRestaurante) {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.agregarRestauranteALista(AuthUtil.getUsuarioLogueado(), idRestaurante));
    }

    @GetMapping("/mostrar-listafav")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> verListaFavoritos() {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.verListaFavoritos(AuthUtil.getUsuarioLogueado()));
    }

    @DeleteMapping("/eliminar-listafav/{id-restaurante}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> eliminarRestauranteDeLista(@PathVariable("id-restaurante") Long idRestaurante) {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.eliminarRestauranteDeLista(AuthUtil.getUsuarioLogueado(), idRestaurante));
    }

    @PostMapping("/calificar-repartidor")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> calificarRepartidor(@RequestBody Map<String, Object> requestBody) {
        try {
            Long pedidoId = ((Number) requestBody.get("pedidoId")).longValue();
            double calificacion = ((Number) requestBody.get("calificacion")).doubleValue();

            clienteService.calificarRepartidor(AuthUtil.getUsuarioLogueado(), pedidoId, calificacion);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseBuilder.success("Repartidor calificado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseBuilder.error(e.getMessage()));
        }
    }

}
