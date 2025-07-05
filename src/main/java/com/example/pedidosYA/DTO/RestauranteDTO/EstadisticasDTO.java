package com.example.pedidosYA.DTO.RestauranteDTO;

public class EstadisticasDTO {
    private int cantidadPedidos;
    private double ingresosTotales;
    private double calificacionPromedio;

    public EstadisticasDTO(int cantidadPedidos, double ingresosTotales, double calificacionPromedio) {
        this.cantidadPedidos = cantidadPedidos;
        this.ingresosTotales = ingresosTotales;
        this.calificacionPromedio = calificacionPromedio;
    }

    public int getCantidadPedidos() {
        return cantidadPedidos;
    }

    public void setCantidadPedidos(int cantidadPedidos) {
        this.cantidadPedidos = cantidadPedidos;
    }

    public double getIngresosTotales() {
        return ingresosTotales;
    }

    public void setIngresosTotales(double ingresosTotales) {
        this.ingresosTotales = ingresosTotales;
    }

    public double getCalificacionPromedio() {
        return calificacionPromedio;
    }

    public void setCalificacionPromedio(double calificacionPromedio) {
        this.calificacionPromedio = calificacionPromedio;
    }
}
