package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.PedidoDTO.PedidoDetailDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoResumenDTO;
import com.example.pedidosYA.DTO.ProductoDTO.ProductoCrearDTO;
import com.example.pedidosYA.DTO.ProductoDTO.ProductoDetailDTO;
import com.example.pedidosYA.DTO.ProductoDTO.ProductoModificarDTO;
import com.example.pedidosYA.DTO.ProductoDTO.ProductoResumenDTO;
import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaResumenDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteCrearDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteDetailDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteModificarDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteResponseDTO;
import com.example.pedidosYA.DTO.ProductoDTO.BuscarProductoDTO;
import com.example.pedidosYA.DTO.PedidoDTO.EstadoPedidoDTO;
import com.example.pedidosYA.Security.AuthUtil;
import com.example.pedidosYA.Service.PedidoService;
import com.example.pedidosYA.Service.ProductoService;
import com.example.pedidosYA.Service.ReseniaService;
import com.example.pedidosYA.Service.RestauranteService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    RestauranteService restauranteService;

    @Autowired
    ProductoService productoService;

    @Autowired
    PedidoService pedidoService;

    @Autowired
    ReseniaService reseniaService;

    @GetMapping("/perfiles")
    @PreAuthorize("hasRole('RESTAURANTE')")
    public ResponseEntity<RestauranteDetailDTO> verRestaurante (){
        return ResponseEntity.ok(restauranteService.findRestauranteByNombre(AuthUtil.getUsuarioLogueado()));
    }

    @PutMapping("/contrasenias")
    @PreAuthorize("hasRole('RESTAURANTE')")
    public ResponseEntity<?> modificarContraseniaRestaurante (@Valid @RequestBody RestauranteModificarDTO restauranteModificarDTO){
        restauranteService.modificarContraseniaRestaurante(AuthUtil.getUsuarioLogueado(), restauranteModificarDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Contrasenia cambiada con exito!");
    }

    @PutMapping("/perfiles")
    @PreAuthorize("hasRole('RESTAURANTE')")
    public ResponseEntity<?> modificarUsuarioNombreRestaurante (@Valid @RequestBody RestauranteModificarDTO restauranteModificarDTO){
        restauranteService.modificarUsuarioNombreRestaurante(AuthUtil.getUsuarioLogueado(), restauranteModificarDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario y/o Nombre cambiados con exito!");
    }

    @PostMapping("/productos")
    @PreAuthorize("hasRole('RESTAURANTE')")
    public ResponseEntity<ProductoDetailDTO> crearProducto (@Valid @RequestBody ProductoCrearDTO productoCrearDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crearProducto(AuthUtil.getUsuarioLogueado(),productoCrearDTO));
    }

    @GetMapping ("/productos")
    public ResponseEntity<?> findALlProducto(){
        return ResponseEntity.ok(productoService.findAllProductosByRestaurante(AuthUtil.getUsuarioLogueado()));
    }

    @PostMapping("/productos/buscar")
    public ResponseEntity<ProductoDetailDTO> findProductoPorNombre(@Valid @RequestBody BuscarProductoDTO buscarDTO){
        return ResponseEntity.ok(productoService.findProductoBynombre(AuthUtil.getUsuarioLogueado(), buscarDTO.getNombre())
        );
    }

    @PutMapping("/productos/{id-producto}")
    @PreAuthorize("hasRole('RESTAURANTE')")
    public ResponseEntity<ProductoDetailDTO> modificarProducto(@PathVariable("id-producto") Long idProducto, @Valid @RequestBody ProductoModificarDTO productoNuevo){
        return ResponseEntity.ok(productoService.modificarProducto(AuthUtil.getUsuarioLogueado(), idProducto, productoNuevo));
    }

    @DeleteMapping("/productos/{id-producto}")
    @PreAuthorize("hasRole('RESTAURANTE')")
    public ResponseEntity<?> eliminarProducto(@PathVariable("id-producto") Long idProducto){
        productoService.eliminarProducto(AuthUtil.getUsuarioLogueado(), idProducto);
        return ResponseEntity.status(HttpStatus.OK).body("Producto con id: "+idProducto+" eliminado");
    }

    @PutMapping("/pedidos/{id-pedido}/estado")
    @PreAuthorize("hasRole('RESTAURANTE')")
    public ResponseEntity<PedidoDetailDTO> modificarEstadoPedido(@PathVariable("id-pedido") Long idPedido, @Valid @RequestBody EstadoPedidoDTO estadoDTO) {
        return ResponseEntity.ok(
                pedidoService.modificarEstadoPedido(idPedido, estadoDTO.getEstado())
        );
    }

    @GetMapping("/pedidos-en-curso")
    public ResponseEntity<List<PedidoResumenDTO>> verPedidosDeRestauranteEnCurso(){
        return ResponseEntity.ok(pedidoService.verPedidosDeRestauranteEnCurso(AuthUtil.getUsuarioLogueado()));
    }

    @GetMapping("/historial-pedidos")
    public ResponseEntity<List<PedidoResumenDTO>> verHistorialPedidosDeRestaurante(){
        return ResponseEntity.ok(pedidoService.verHistorialPedidosDeRestaurante(AuthUtil.getUsuarioLogueado()));
    }

    @GetMapping("/resenias")
    public ResponseEntity<List<ReseniaResumenDTO>> verReseniasRestaurante(){
        return ResponseEntity.ok(reseniaService.verReseniasRestaurante(AuthUtil.getUsuarioLogueado()));
    }
}
