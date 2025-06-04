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
}
