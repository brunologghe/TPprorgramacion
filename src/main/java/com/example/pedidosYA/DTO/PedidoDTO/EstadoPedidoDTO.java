package com.example.pedidosYA.DTO.PedidoDTO;

import jakarta.validation.constraints.NotBlank;

public class EstadoPedidoDTO {
    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}