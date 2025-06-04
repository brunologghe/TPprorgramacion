package com.example.pedidosYA.DTO.PedidoDTO;

import jakarta.validation.constraints.NotNull;

public class DetallePedidoDTO {
    @NotNull(message = "El producto no puede ser nulo")
    private Long productoId;
    @NotNull(message = "El pago no puede ser nula")
    private Integer cantidad;
}
