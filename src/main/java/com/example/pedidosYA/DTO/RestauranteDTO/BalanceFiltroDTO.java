package com.example.pedidosYA.DTO.RestauranteDTO;


public class BalanceFiltroDTO {

    private String tipoFiltro; // "dia" o "mes"

    private String fecha;      // formato: "2025-11-23" (para filtro por día)
    private String mes;        // formato: "2025-11" (para filtro por mes)

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