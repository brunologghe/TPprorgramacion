package com.example.pedidosYA.DTO.PedidoDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DetallePedidoDTO {

    @NotNull(message = "El producto no puede ser nulo")
    @Min(value = 1, message = "El productoId debe ser mayor o igual a 1")
    private Long productoId;

    private String nombreProducto;

    private Double precioUnitario;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    public DetallePedidoDTO() {
    }

    // Constructor que seguís usando para CREAR pedidos (id + cantidad)
    public DetallePedidoDTO(Long productoId, Integer cantidad) {
        this.productoId = productoId;
        this.cantidad = cantidad;
    }

    // Constructor completo para RESPUESTAS (id + nombre + precio + cantidad)
    public DetallePedidoDTO(Long productoId, String nombreProducto, Double precioUnitario, Integer cantidad) {
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
