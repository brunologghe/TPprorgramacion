package com.example.pedidosYA.DTO.PedidoDTO;

import jakarta.validation.constraints.NotNull;

public class DetallePedidoDTO {
    @NotNull(message = "El producto no puede ser nulo")
    private Long productoId;
    @NotNull(message = "El pago no puede ser nula")
    private Integer cantidad;
    public @NotNull(message = "El producto no puede ser nulo") Long getProductoId() {
        return productoId;
    }

    public void setProductoId(@NotNull(message = "El producto no puede ser nulo") Long productoId) {
        this.productoId = productoId;
    }

    public @NotNull(message = "El pago no puede ser nula") Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(@NotNull(message = "El pago no puede ser nula") Integer cantidad) {
        this.cantidad = cantidad;
    }
}
