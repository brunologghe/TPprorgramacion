package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.ClienteDTO.ClienteDetailDto;
import com.example.pedidosYA.DTO.DireccionDTO.DireccionCrearDTO;
import com.example.pedidosYA.DTO.DireccionDTO.DireccionDTO;
import com.example.pedidosYA.DTO.DireccionDTO.DireccionEliminarDTO;
import com.example.pedidosYA.Security.AuthUtil;
import com.example.pedidosYA.Service.DireccionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/direcciones")
public class DireccionController {

    @Autowired
    private DireccionService direccionService;
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<DireccionDTO> crearDireccion(@Valid @RequestBody DireccionCrearDTO dire) {
        String username = AuthUtil.getUsuarioLogueado();
        return ResponseEntity.status(HttpStatus.CREATED).body(direccionService.crearDireccion(username, dire));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public void eliminarDireccion(@RequestBody DireccionEliminarDTO dire) {
        String username = AuthUtil.getUsuarioLogueado();
        direccionService.eliminarDireccion(username, dire);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<DireccionDTO> modificar(@PathVariable Long id, @RequestBody DireccionCrearDTO dto) {
        String username = AuthUtil.getUsuarioLogueado();
        return ResponseEntity.ok(direccionService.modificarDireccion(username, id, dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public List<DireccionDTO> listarDirecciones() {
        String username = AuthUtil.getUsuarioLogueado();
        return direccionService.listarDirecciones(username);
    }

}
