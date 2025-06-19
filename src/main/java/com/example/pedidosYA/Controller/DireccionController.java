package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.ClienteDTO.ClienteDetailDto;
import com.example.pedidosYA.DTO.DireccionDTO.DireccionCrearDTO;
import com.example.pedidosYA.DTO.DireccionDTO.DireccionDTO;
import com.example.pedidosYA.DTO.DireccionDTO.DireccionEliminarDTO;
import com.example.pedidosYA.Service.DireccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/direccion")
public class DireccionController {

    @Autowired
    private DireccionService direccionService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<DireccionDTO> crearDireccion(@RequestBody DireccionCrearDTO dire)
    {
        DireccionDTO diredto = direccionService.crearDireccion(dire);
        return ResponseEntity.status(HttpStatus.CREATED).body(diredto);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public void eliminarDireccion(@RequestBody DireccionEliminarDTO dire)
    {
        direccionService.eliminarDireccion(dire);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<DireccionDTO> modificar(@PathVariable Long id, @RequestBody DireccionCrearDTO dto) {
        return ResponseEntity.ok(direccionService.modificarDireccion(id, dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public List<DireccionDTO>listarDirecciones(@PathVariable Long id){
        return direccionService.listarDirecciones(id);
    }
}
