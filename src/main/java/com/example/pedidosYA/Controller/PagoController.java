package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.PagoDTO.PagoMuestraDTO;
import com.example.pedidosYA.DTO.PagoDTO.PagoRequestDTO;
import com.example.pedidosYA.Model.MetodoDePago;
import com.example.pedidosYA.Model.Pago;
import com.example.pedidosYA.Security.AuthUtil;
import com.example.pedidosYA.Service.PagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pago")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<PagoMuestraDTO> crearPago(@Valid @RequestBody PagoRequestDTO metodo) {
        String username = AuthUtil.getUsuarioLogueado();
        PagoMuestraDTO pago = pagoService.agregarPago(username, metodo);
        return ResponseEntity.status(HttpStatus.CREATED).body(pago);
    }

    @DeleteMapping("/{idPago}")
    @PreAuthorize("hasRole('CLIENTE')")
    public void eliminarPago(@PathVariable Long idPago) {
        String username = AuthUtil.getUsuarioLogueado();
        pagoService.eliminarPago(username, idPago);
    }

    @GetMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public List<Pago> mostrarPagos() {
        String username = AuthUtil.getUsuarioLogueado();
        return pagoService.mostarPagos(username);
    }

}
