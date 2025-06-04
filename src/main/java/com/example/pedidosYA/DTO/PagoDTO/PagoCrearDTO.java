package com.example.pedidosYA.DTO.PagoDTO;

import com.example.pedidosYA.Model.MetodoDePago;
import jakarta.validation.constraints.NotBlank;

public class PagoCrearDTO {
    @NotBlank(message = "El metodo de pago es obligatorio")
    private MetodoDePago metodoDePago;

}
