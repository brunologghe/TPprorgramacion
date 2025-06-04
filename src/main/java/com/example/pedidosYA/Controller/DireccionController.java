package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.ClienteDTO.ClienteDetailDto;
import com.example.pedidosYA.DTO.DireccionDTO.DireccionCrearDTO;
import com.example.pedidosYA.DTO.DireccionDTO.DireccionDTO;
import com.example.pedidosYA.Service.DireccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/direccion")
public class DireccionController {

    @Autowired
    private DireccionService direccionService;

    @PostMapping
    public ResponseEntity<DireccionDTO> crearDireccion(@RequestBody DireccionCrearDTO dire)
    {
        DireccionDTO diredto = direccionService.crearDireccion(dire);
        return ResponseEntity.status(HttpStatus.CREATED).body(diredto);
    }

}
