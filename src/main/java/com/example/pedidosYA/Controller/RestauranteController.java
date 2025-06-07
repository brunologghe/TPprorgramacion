package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.ProductoDTO.ProductoCrearDTO;
import com.example.pedidosYA.DTO.ProductoDTO.ProductoDetailDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteCrearDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteDetailDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteModificarDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteResponseDTO;
import com.example.pedidosYA.Service.ProductoService;
import com.example.pedidosYA.Service.RestauranteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    RestauranteService restauranteService;

    @Autowired
    ProductoService productoService;

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

    @PostMapping("/producto")
    public ResponseEntity<ProductoDetailDTO> crearProducto (@Valid @RequestBody ProductoCrearDTO productoCrearDTO){

        ProductoDetailDTO bodyProducto = productoService.crearProducto(productoCrearDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(bodyProducto);
    }

    @GetMapping ("/producto/{id}")
    public ResponseEntity<?> findALlProducto(@PathVariable Long id){
        return ResponseEntity.ok(productoService.findAllProductosByRestauranteId(id));
    }
}
