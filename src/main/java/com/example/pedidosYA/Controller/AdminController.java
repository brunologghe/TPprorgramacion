package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.ClienteDTO.ModificarDTO;
import com.example.pedidosYA.DTO.ClienteDTO.ResponseDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteModificarDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteResponseDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteResumenDTO;
import com.example.pedidosYA.Service.ClienteService;
import com.example.pedidosYA.Service.RestauranteService;
import jakarta.persistence.DiscriminatorValue;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/admin")
@DiscriminatorValue("ADMIN")
public class AdminController {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private RestauranteService restauranteService;


    @GetMapping("/cliente")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ResponseDTO>> listAllClientes(){
        return ResponseEntity.ok(clienteService.listAll());
    }

    @DeleteMapping("/cliente/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> deleteCliente(@PathVariable Long id){
        return ResponseEntity.ok(clienteService.eliminar(id));
    }

    @PutMapping ("/cliente/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> modificarCliente (@PathVariable Long id, @Valid @RequestBody ModificarDTO modificarDTO){

        ResponseDTO cliente = clienteService.modificar(id, modificarDTO);

        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/restaurante")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<RestauranteResumenDTO>> listAllRestaurantes(){
        return ResponseEntity.ok(restauranteService.findAllRestaurantes());
    }

    @DeleteMapping("/restaurante/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestauranteResponseDTO> deleteRestaurante(@PathVariable Long id){
        return ResponseEntity.ok(restauranteService.eliminarRestaurante(id));
    }

    @PutMapping ("/restaurante/{nombre}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestauranteResponseDTO> modificarRestaurante (@PathVariable String nombre, @Valid @RequestBody RestauranteModificarDTO restauranteModificarDTO){

        RestauranteResponseDTO bodyRestaurante = restauranteService.modificarRestaurante(nombre, restauranteModificarDTO);

        return ResponseEntity.ok(bodyRestaurante);
    }


}
