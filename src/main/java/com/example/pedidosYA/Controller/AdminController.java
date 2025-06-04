package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.AdminDTO.AdminDetailDTO;
import com.example.pedidosYA.DTO.AdminDTO.AdminRequestDTO;
import com.example.pedidosYA.Service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AdminDetailDTO> crear(@Valid @RequestBody AdminRequestDTO req) {
        AdminDetailDTO creado = service.crearAdmin(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }


}
