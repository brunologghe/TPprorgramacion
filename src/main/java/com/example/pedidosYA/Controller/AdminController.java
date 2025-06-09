package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.AdminDTO.AdminDetailDTO;
import com.example.pedidosYA.DTO.AdminDTO.AdminRequestDTO;
import com.example.pedidosYA.DTO.ClienteDTO.ResponseDTO;
import com.example.pedidosYA.Service.AdminService;
import com.example.pedidosYA.Service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private ClienteService clienteService;


    @PostMapping
    public ResponseEntity<AdminDetailDTO> crear(@Valid @RequestBody AdminRequestDTO req) {
        AdminDetailDTO creado = adminService.crearAdmin(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/cliente")
    public ResponseEntity<List<ResponseDTO>> listAll(){
        return ResponseEntity.ok(clienteService.listAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable Long id){
        return ResponseEntity.ok(clienteService.eliminar(id));
    }


}
