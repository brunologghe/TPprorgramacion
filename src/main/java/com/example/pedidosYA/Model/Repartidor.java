package com.example.pedidosYA.Model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Repartidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @Column
    private String apellido;

    @Column
    private String email;

    @Column
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column
    private TipoVehiculo tipoVehiculo;

    @Column
    private Boolean disponible;

    @Column
    private Boolean trabajando;

    @ElementCollection
    @CollectionTable(name = "repartidor_zonas", joinColumns = @JoinColumn(name = "repartidor_id"))
    @Column(name = "codigo_postal")
    private List<String> zonas = new ArrayList<>();

    @Column
    private Long pedidoActualId;

    @Column
    private Integer totalPedidosEntregados;

    @Column
    private Double calificacionPromedio;

    public Repartidor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public List<String> getZonas() {
        return zonas;
    }

    public void setZonas(List<String> zonas) {
        this.zonas = zonas;
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
}
