package com.example.pedidosYA.DTO.PedidoDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DetallePedidoDTO {
    @NotNull(message = "El producto no puede ser nulo")
    @Min(value = 1, message = "El productoId debe ser mayor o igual a 1")
    private Long productoId;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
    public @NotNull(message = "El producto no puede ser nulo") Long getProductoId() {
        return productoId;
    }

    public DetallePedidoDTO() {
    }

    public DetallePedidoDTO(Long productoId, Integer cantidad) {
        this.productoId = productoId;
        this.cantidad = cantidad;
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
