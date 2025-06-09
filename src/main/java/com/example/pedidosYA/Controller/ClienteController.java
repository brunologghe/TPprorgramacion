package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.ClienteDTO.ClienteCrearDTO;
import com.example.pedidosYA.DTO.ClienteDTO.ClienteDetailDto;
import com.example.pedidosYA.DTO.ClienteDTO.ResponseDTO;
import com.example.pedidosYA.Service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
@PreAuthorize("hasRole('CLIENTE')")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ResponseDTO> crear (@Valid @RequestBody ClienteCrearDTO cliente)
    {
        ResponseDTO bodyCliente = clienteService.crearUsuario(cliente);

        return ResponseEntity.status(HttpStatus.CREATED).body(bodyCliente);
    }

    @GetMapping("/{id}")
    public ClienteDetailDto verCliente(@PathVariable Long id)
    {
        return clienteService.verUsuario(id);
    }


}
