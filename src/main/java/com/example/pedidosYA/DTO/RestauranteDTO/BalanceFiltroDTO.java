package com.example.pedidosYA.DTO.RestauranteDTO;

import jakarta.validation.constraints.NotNull;

public class BalanceFiltroDTO {

    @NotNull(message = "El tipo de filtro es obligatorio")
    private String tipoFiltro; // "dia" o "mes"

    private String fecha; // formato: "2024-11-20"

    private String mes; // formato: "2024-11"

    public BalanceFiltroDTO() {}

    public BalanceFiltroDTO(String tipoFiltro, String fecha, String mes) {
        this.tipoFiltro = tipoFiltro;
        this.fecha = fecha;
        this.mes = mes;
    }

    public String getTipoFiltro() {
        return tipoFiltro;
    }

    public void setTipoFiltro(String tipoFiltro) {
        this.tipoFiltro = tipoFiltro;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }
}