package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.PedidoDTO.PedidoDetailDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoResumenDTO;
import com.example.pedidosYA.DTO.ProductoDTO.ProductoCrearDTO;
import com.example.pedidosYA.DTO.ProductoDTO.ProductoDetailDTO;
import com.example.pedidosYA.DTO.ProductoDTO.ProductoModificarDTO;
import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaResumenDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteCrearDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteDetailDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteModificarDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteResponseDTO;
import com.example.pedidosYA.Service.PedidoService;
import com.example.pedidosYA.Service.ProductoService;
import com.example.pedidosYA.Service.ReseniaService;
import com.example.pedidosYA.Service.RestauranteService;
import jakarta.validation.Valid;
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

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(restauranteService.findAllRestaurantes());
    }

    @PostMapping
    public ResponseEntity<RestauranteResponseDTO> crearRestaurante (@Valid @RequestBody RestauranteCrearDTO restaurante)
    {
        RestauranteResponseDTO bodyRestaurante = restauranteService.crearRestaurante(restaurante);

        return ResponseEntity.status(HttpStatus.CREATED).body(bodyRestaurante);
    }

    @GetMapping ("/{nombre}")
    public ResponseEntity<RestauranteDetailDTO> findByNombre (@PathVariable String nombre){
        return ResponseEntity.ok(restauranteService.findRestauranteByNombre(nombre));
    }

    @PutMapping ("/{id}")
    public ResponseEntity<RestauranteResponseDTO> modificarRestaurante (@PathVariable Long id, @Valid @RequestBody RestauranteModificarDTO restauranteModificarDTO){

        RestauranteResponseDTO bodyRestaurante = restauranteService.modificarRestaurante(id, restauranteModificarDTO);

        return ResponseEntity.ok(bodyRestaurante);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestauranteDetailDTO> eliminarRestaurante(@PathVariable Long id){
        restauranteService.eliminarRestaurante(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/productos")
    public ResponseEntity<ProductoDetailDTO> crearProducto (@Valid @RequestBody ProductoCrearDTO productoCrearDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crearProducto(productoCrearDTO));
    }

    @GetMapping ("/productos/{id}")
    public ResponseEntity<?> findALlProducto(@PathVariable Long id){
        return ResponseEntity.ok(productoService.findAllProductosByRestauranteId(id));
    }

    @GetMapping ("/productos/{idRestaurante}/{nombre}")
    public ResponseEntity<ProductoDetailDTO> findProductoBynombreAndIdRestaurante(@PathVariable Long idRestaurante, @PathVariable String nombre){
        return ResponseEntity.ok(productoService.findProductoBynombreAndIdRestaurante(idRestaurante, nombre));
    }

    @PutMapping ("/productos/{idRestaurante}/{idProducto}")
    public ResponseEntity<ProductoDetailDTO> modificarProducto(@PathVariable Long idRestaurante, @PathVariable Long idProducto, @Valid @RequestBody ProductoModificarDTO productoNuevo){
        return ResponseEntity.ok(productoService.modificarProducto(idRestaurante, idProducto, productoNuevo));
    }

    @DeleteMapping ("/productos/{idRestaurante}/{idProducto}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long idRestaurante, @PathVariable Long idProducto){
        productoService.eliminarProducto(idRestaurante, idProducto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping ("/pedidos/{idPedido}/{estado}")
    public ResponseEntity<PedidoDetailDTO>modificarEstadoPedido(@PathVariable Long idPedido, @PathVariable String estado){
        return ResponseEntity.ok(pedidoService.modificarEstadoPedido(idPedido, estado));
    }

    @GetMapping("/pedidos-en-curso/{id}")
    public ResponseEntity<List<PedidoResumenDTO>> verPedidosDeRestauranteEnCurso(@PathVariable Long id){
        return ResponseEntity.ok(pedidoService.verPedidosDeRestauranteEnCurso(id));
    }

    @GetMapping("/historial-pedidos/{id}")
    public ResponseEntity<List<PedidoResumenDTO>> verHistorialPedidosDeRestaurante(@PathVariable Long id){
        return ResponseEntity.ok(pedidoService.verHistorialPedidosDeRestaurante(id));
    }

    @GetMapping("/resenias/{id}")
    public ResponseEntity<List<ReseniaResumenDTO>> verReseniasRestaurante(@PathVariable Long id){
        return ResponseEntity.ok(reseniaService.verReseniasRestaurante(id));
    }
}
