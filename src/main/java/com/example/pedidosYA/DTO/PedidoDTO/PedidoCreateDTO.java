package com.example.pedidosYA.DTO.PedidoDTO;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PedidoCreateDTO {

    @NotNull(message = "El restaurante no puede ser nulo")
    private Long restauranteId;
    @NotNull(message = "La direccion no puede ser nula")
    private Long direccionId;
    @NotNull(message = "El pago no puede ser nulo")
    private Long pagoId;
    @NotNull(message = "Los detalles no puede ser nulos")
    private List<DetallePedidoDTO> detalles;

    public @NotNull(message = "El restaurante no puede ser nulo") Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(@NotNull(message = "El restaurante no puede ser nulo") Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public @NotNull(message = "La direccion no puede ser nula") Long getDireccionId() {
        return direccionId;
    }

    public void setDireccionId(@NotNull(message = "La direccion no puede ser nula") Long direccionId) {
        this.direccionId = direccionId;
    }

    public @NotNull(message = "El pago no puede ser nulo") Long getPagoId() {
        return pagoId;
    }

    public void setPagoId(@NotNull(message = "El pago no puede ser nulo") Long pagoId) {
        this.pagoId = pagoId;
    }

    public @NotNull(message = "Los detalles no puede ser nulos") List<DetallePedidoDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(@NotNull(message = "Los detalles no puede ser nulos") List<DetallePedidoDTO> detalles) {
        this.detalles = detalles;
    }
}
