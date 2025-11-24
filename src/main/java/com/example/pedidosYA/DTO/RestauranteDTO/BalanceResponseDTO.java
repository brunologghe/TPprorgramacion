package com.example.pedidosYA.DTO.RestauranteDTO;

public class BalanceResponseDTO {
    private Double totalRecaudado;
    private Integer cantidadPedidos;
    private Double promedioVenta;

    public BalanceResponseDTO() {}

    public BalanceResponseDTO(Double totalRecaudado, Integer cantidadPedidos, Double promedioVenta) {
        this.totalRecaudado = totalRecaudado;
        this.cantidadPedidos = cantidadPedidos;
        this.promedioVenta = promedioVenta;
    }

    public Double getTotalRecaudado() {
        return totalRecaudado;
    }

    public void setTotalRecaudado(Double totalRecaudado) {
        this.totalRecaudado = totalRecaudado;
    }

    public Integer getCantidadPedidos() {
        return cantidadPedidos;
    }

    public void setCantidadPedidos(Integer cantidadPedidos) {
        this.cantidadPedidos = cantidadPedidos;
    }

    public Double getPromedioVenta() {
        return promedioVenta;
    }

    public void setPromedioVenta(Double promedioVenta) {
        this.promedioVenta = promedioVenta;
    }
}