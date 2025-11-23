package com.example.pedidosYA.DTO.RestauranteDTO;

import jakarta.validation.constraints.NotBlank;

public class BalanceFiltroDTO {

    @NotBlank(message = "El tipo de filtro es obligatorio")
    private String tipoFiltro; // "dia" o "mes"

    private String fecha;      // formato: "2025-11-23" (para filtro por día)
    private String mes;        // formato: "2025-11" (para filtro por mes)

    public BalanceFiltroDTO() {}

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