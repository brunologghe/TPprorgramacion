package com.example.pedidosYA.Model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("REPARTIDOR")
public class Repartidor extends Usuario {

    @Column
    private String nombreYapellido;

    @Column
    private String pais;

    @Enumerated(EnumType.STRING)
    @Column
    private TipoVehiculo tipoVehiculo;

    @Column
    private Boolean disponible;

    @Column
    private Boolean trabajando;

    @Column
    private Long pedidoActualId;

    @OneToMany(mappedBy = "repartidor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resenia> reseniasRepartidor;

    @Column
    private Integer totalPedidosEntregados;

    @Column
    private Double calificacionPromedio;

    public Repartidor() {
    }

    public String getNombreYapellido() {
        return nombreYapellido;
    }

    public void setNombreYapellido(String nombreYapellido) {
        this.nombreYapellido = nombreYapellido;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Boolean getTrabajando() {
        return trabajando;
    }

    public void setTrabajando(Boolean trabajando) {
        this.trabajando = trabajando;
    }

    public Long getPedidoActualId() {
        return pedidoActualId;
    }

    public void setPedidoActualId(Long pedidoActualId) {
        this.pedidoActualId = pedidoActualId;
    }

    public Integer getTotalPedidosEntregados() {
        return totalPedidosEntregados;
    }

    public void setTotalPedidosEntregados(Integer totalPedidosEntregados) {
        this.totalPedidosEntregados = totalPedidosEntregados;
    }

    public Double getCalificacionPromedio() {
        return calificacionPromedio;
    }

    public void setCalificacionPromedio(Double calificacionPromedio) {
        this.calificacionPromedio = calificacionPromedio;
    }

    public List<Resenia> getReseniasRepartidor() {
        return reseniasRepartidor;
    }

    public void setReseniasRepartidor(List<Resenia> reseniasRepartidor) {
        this.reseniasRepartidor = reseniasRepartidor;
    }
}
