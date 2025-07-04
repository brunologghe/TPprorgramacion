
package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.ClienteDTO.ClienteCrearDTO;
import com.example.pedidosYA.DTO.ClienteDTO.ClienteDetailDto;
import com.example.pedidosYA.DTO.ClienteDTO.ModificarDTO;
import com.example.pedidosYA.DTO.ClienteDTO.ResponseDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoCreateDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoDetailDTO;
import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaCreateDTO;
import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaDetailDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteResumenDTO;
import com.example.pedidosYA.Security.AuthUtil;
import com.example.pedidosYA.Service.*;
import jakarta.persistence.DiscriminatorValue;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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


    @GetMapping("/perfiles")
    @PreAuthorize("hasRole('CLIENTE')")
    public ClienteDetailDto verCliente() {
        return clienteService.verUsuarioPorNombre(AuthUtil.getUsuarioLogueado());
    }

    @PutMapping("/perfiles")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> modificarUsuarioNombreCliente (@Valid @RequestBody ModificarDTO modificarDTO){
        clienteService.modificarUsuarioNombre(AuthUtil.getUsuarioLogueado(), modificarDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario y/o Nombre cambiados con exito!");
    }

    @PutMapping("/contrasenias")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> modificarContraseniaCliente (@Valid @RequestBody ModificarDTO modificarDTO){
        clienteService.modificarContrasenia(AuthUtil.getUsuarioLogueado(), modificarDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Contrasenia cambiada con exito!");
    }

    @GetMapping("/restaurantes")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Set<RestauranteResumenDTO>> listAllRestaurantes(){
        return ResponseEntity.ok(restauranteService.findAllRestaurantes());
    }

    @GetMapping ("/ver-menu/{nombre}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> findALlProducto(@PathVariable String nombre){
        return ResponseEntity.ok(productoService.findAllProductosByRestauranteNombre(nombre));
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

    @DeleteMapping("/{id-pedido}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> cancelarPedido(@PathVariable("id-producto") Long idPedido) {
        pedidoService.cancelarPedido(AuthUtil.getUsuarioLogueado(),idPedido);
        return ResponseEntity.status(HttpStatus.OK).body("Pedido con id: "+idPedido+" eliminado");
    }
}
