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
@RequestMapping("/restaurante")
public class RestauranteController {

    @Autowired
    RestauranteService restauranteService;

    @Autowired
    ProductoService productoService;

    @Autowired
    PedidoService pedidoService;

    @Autowired
    ReseniaService reseniaService;

    @GetMapping("/perfil")
    @PreAuthorize("hasRole('RESTAURANTE')")
    public ResponseEntity<RestauranteDetailDTO> verRestaurante (){
        return ResponseEntity.ok(restauranteService.findRestauranteByNombre(AuthUtil.getUsuarioLogueado()));
    }

    @PutMapping
    @PreAuthorize("hasRole('RESTAURANTE')")
    public ResponseEntity<RestauranteResponseDTO> modificarRestaurante (@Valid @RequestBody RestauranteModificarDTO restauranteModificarDTO){
        return ResponseEntity.ok(restauranteService.modificarRestaurante(AuthUtil.getUsuarioLogueado(), restauranteModificarDTO));
    }

    @PostMapping("/producto")
    @PreAuthorize("hasRole('RESTAURANTE')")
    public ResponseEntity<ProductoDetailDTO> crearProducto (@Valid @RequestBody ProductoCrearDTO productoCrearDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crearProducto(AuthUtil.getUsuarioLogueado(),productoCrearDTO));
    }

    @GetMapping ("/producto")
    public ResponseEntity<?> findALlProducto(){
        return ResponseEntity.ok(productoService.findAllProductosByRestaurante(AuthUtil.getUsuarioLogueado()));
    }

    @GetMapping ("/producto/{nombre}")
    public ResponseEntity<ProductoDetailDTO> findProductoBynombre(@PathVariable String nombre){
        return ResponseEntity.ok(productoService.findProductoBynombre(AuthUtil.getUsuarioLogueado(), nombre));
    }

    @PutMapping  ("/producto/{idProducto}")
    @PreAuthorize("hasRole('RESTAURANTE')")
    public ResponseEntity<ProductoDetailDTO> modificarProducto(@PathVariable Long idProducto, @Valid @RequestBody ProductoModificarDTO productoNuevo){
        return ResponseEntity.ok(productoService.modificarProducto(AuthUtil.getUsuarioLogueado(), idProducto, productoNuevo));
    }

    @DeleteMapping ("/producto/{idProducto}")
    @PreAuthorize("hasRole('RESTAURANTE')")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long idProducto){
        productoService.eliminarProducto(AuthUtil.getUsuarioLogueado(), idProducto);
        return ResponseEntity.status(HttpStatus.OK).body("Producto con id: "+idProducto+" eliminado");
    }

    @PutMapping ("/pedidos/{idPedido}/{estado}")
    @PreAuthorize("hasRole('RESTAURANTE')")
    public ResponseEntity<PedidoDetailDTO>modificarEstadoPedido(@PathVariable Long idPedido, @PathVariable String estado){
        return ResponseEntity.ok(pedidoService.modificarEstadoPedido(idPedido, estado));
    }

    @GetMapping("/pedidosEnCurso")
    public ResponseEntity<List<PedidoResumenDTO>> verPedidosDeRestauranteEnCurso(){
        return ResponseEntity.ok(pedidoService.verPedidosDeRestauranteEnCurso(AuthUtil.getUsuarioLogueado()));
    }

    @GetMapping("/historialPedidos")
    public ResponseEntity<List<PedidoResumenDTO>> verHistorialPedidosDeRestaurante(){
        return ResponseEntity.ok(pedidoService.verHistorialPedidosDeRestaurante(AuthUtil.getUsuarioLogueado()));
    }

    @GetMapping("/resenias")
    public ResponseEntity<List<ReseniaResumenDTO>> verReseniasRestaurante(){
        return ResponseEntity.ok(reseniaService.verReseniasRestaurante(AuthUtil.getUsuarioLogueado()));
    }
}
