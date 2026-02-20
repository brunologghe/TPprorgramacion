package com.example.pedidosYA.Model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@DiscriminatorValue("RESTAURANTE")
public class Restaurante extends Usuario{

    @Column
    private String nombre;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Producto> menu;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resenia> reseniasRestaurante;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pedido> pedidos;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL)
    private List<Direccion> direcciones;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRestaurante estado = EstadoRestaurante.PENDIENTE;

    @Column(length = 500)
    private String motivoRechazo;

    public EstadoRestaurante getEstado() {
        return estado;
    }

    public void setEstado(EstadoRestaurante estado) {
        this.estado = estado;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    @Column(name = "hora_apertura")
    private LocalTime horaApertura;

    @Column(name = "hora_cierre")
    private LocalTime horaCierre;


    public Restaurante() {
    }

    public LocalTime getHoraApertura() {
        return horaApertura;
    }

    public void setHoraApertura(LocalTime horaApertura) {
        this.horaApertura = horaApertura;
    }

    public LocalTime getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(LocalTime horaCierre) {
        this.horaCierre = horaCierre;
    }

    public Restaurante(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Producto> getMenu() {
        return menu;
    }

    public void setMenu(Set<Producto> menu) {
        this.menu = menu;
    }

    public List<Resenia> getReseniasRestaurante() {
        return reseniasRestaurante;
    }

    public void setReseniasRestaurante(List<Resenia> reseniasRestaurante) {
        this.reseniasRestaurante = reseniasRestaurante;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public List<Direccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<Direccion> direcciones) {
        this.direcciones = direcciones;
    }
}
