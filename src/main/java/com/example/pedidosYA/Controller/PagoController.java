package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.PagoDTO.TarjetaMuestraDTO;
import com.example.pedidosYA.DTO.PagoDTO.TarjetaRequestDTO;
import com.example.pedidosYA.DTO.PagoDTO.TarjetaResponseDTO;
import com.example.pedidosYA.Model.Tarjeta;
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
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<TarjetaMuestraDTO> crearTarjeta(@Valid @RequestBody TarjetaRequestDTO tarjetaRequestDTO) {
        String username = AuthUtil.getUsuarioLogueado();
        TarjetaMuestraDTO tarjeta = pagoService.agregarTarjeta(username, tarjetaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarjeta);
    }

    @DeleteMapping("/{id-pago}")
    @PreAuthorize("hasRole('CLIENTE')")
    public void eliminarTarjeta(@PathVariable("id-pago") Long idPago) {
        String username = AuthUtil.getUsuarioLogueado();
        pagoService.eliminarTarjeta(username, idPago);
    }

    @GetMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public List<TarjetaResponseDTO> mostrarTarjetas() {
        String username = AuthUtil.getUsuarioLogueado();
        return pagoService.mostarTarjetas(username);
    }

}
