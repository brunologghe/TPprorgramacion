package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.PagoDTO.PagoMuestraDTO;
import com.example.pedidosYA.DTO.PagoDTO.PagoRequestDTO;
import com.example.pedidosYA.Model.MetodoDePago;
import com.example.pedidosYA.Model.Pago;
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

    @PostMapping("/{idCliente}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<PagoMuestraDTO>crearPago(@PathVariable Long idCliente, @Valid @RequestBody PagoRequestDTO metodo)
    {
        PagoMuestraDTO pago = pagoService.agregarPago(idCliente, metodo);
        return ResponseEntity.status(HttpStatus.CREATED).body(pago);
    }

    @DeleteMapping("/{idCliente}/pago/{idPago}")
    @PreAuthorize("hasRole('CLIENTE')")
    public void eliminarPago(@PathVariable Long idCliente, @PathVariable Long idPago) {
        pagoService.eliminarPago(idCliente, idPago);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public List<Pago>mostrarPagos(@PathVariable Long id)
    {
        return pagoService.mostarPagos(id);
    }
}
