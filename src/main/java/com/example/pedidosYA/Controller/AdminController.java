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
import org.springframework.http.HttpStatus;
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


    @GetMapping("/clientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ResponseDTO>> listAllClientes(){
        return ResponseEntity.ok(clienteService.listAll());
    }

    @DeleteMapping("/clientes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> deleteCliente(@PathVariable Long id){
        return ResponseEntity.ok(clienteService.eliminar(id));
    }

    @PutMapping ("/clientes/{usuario}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> modificarCliente (@PathVariable String usuario, @Valid @RequestBody ModificarDTO modificarDTO){
        clienteService.modificarUsuarioNombreAdmin(usuario, modificarDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario y/o Nombre cambiados con exito!");
    }

    @GetMapping("/restaurantes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<RestauranteResumenDTO>> listAllRestaurantes(){
        return ResponseEntity.ok(restauranteService.findAllRestaurantes());
    }

    @DeleteMapping("/restaurantes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestauranteResponseDTO> deleteRestaurante(@PathVariable Long id){
        return ResponseEntity.ok(restauranteService.eliminarRestaurante(id));
    }

    @PutMapping ("/restaurantes/{usuario}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> modificarRestaurante (@PathVariable String usuario, @Valid @RequestBody RestauranteModificarDTO restauranteModificarDTO){
        restauranteService.modificarUsuarioNombreRestauranteAdmin(usuario, restauranteModificarDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario y/o Nombre cambiados con exito!");
    }


}
