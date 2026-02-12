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

    @ElementCollection
    @CollectionTable(name = "repartidor_zonas", joinColumns = @JoinColumn(name = "repartidor_id"))
    @Column(name = "codigo_postal")
    private List<String> zonas = new ArrayList<>();

    @OneToMany(mappedBy = "repartidor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resenia> reseniasRepartidor = new ArrayList<>();

    @Column
    private Integer totalPedidosEntregados;

    @Column
    private Double calificacionPromedio;

    @Column
    private Boolean activo;

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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<String> getZonas() {
        return zonas;
    }

    public void setZonas(List<String> zonas) {
        this.zonas = zonas;
    }
}
