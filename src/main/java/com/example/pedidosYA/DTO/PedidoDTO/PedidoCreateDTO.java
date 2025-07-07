package com.example.pedidosYA.DTO.PedidoDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class PedidoCreateDTO {

    @NotNull(message = "El restaurante no puede ser nulo")
    @Min(value = 1, message = "El restauranteId debe ser mayor o igual a 1")
    private Long restauranteId;

    @NotNull(message = "La dirección no puede ser nula")
    @Min(value = 1, message = "El direccionId debe ser mayor o igual a 1")
    private Long direccionId;

    @NotNull(message = "El pago no puede ser nulo")
    @Min(value = 1, message = "El pagoId debe ser mayor o igual a 1")
    private Long pagoId;

    @NotNull(message = "Los detalles no pueden ser nulos")
    @NotEmpty(message = "Debe haber al menos un detalle en el pedido")
    @Size(min = 1, message = "Debe haber al menos un detalle en el pedido")
    @Valid
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
