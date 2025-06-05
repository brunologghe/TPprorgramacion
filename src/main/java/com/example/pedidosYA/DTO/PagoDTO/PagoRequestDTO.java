package com.example.pedidosYA.DTO.PagoDTO;

import com.example.pedidosYA.Model.MetodoDePago;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PagoRequestDTO {
    @NotNull(message = "No puede ser nulo el metodo")
    private MetodoDePago metodoDePago;

    public PagoRequestDTO(MetodoDePago metodoDePago) {
        this.metodoDePago = metodoDePago;
    }

    public @NotNull(message = "No puede ser nulo el metodo") MetodoDePago getMetodoDePago() {
        return metodoDePago;
    }

    public void setMetodoDePago(@NotNull(message = "No puede ser nulo el metodo") MetodoDePago metodoDePago) {
        this.metodoDePago = metodoDePago;
    }
}
