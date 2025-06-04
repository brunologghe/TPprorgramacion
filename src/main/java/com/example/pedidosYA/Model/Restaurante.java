package com.example.pedidosYA.Model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
public class Restaurante {

    Long id;
    @Column
    private String nombre;

    @OneToMany
    @JoinColumn(name = "producto_id")
    private Map<Producto, Integer> menu;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resenia> reseniasRestaurante;

    public Restaurante() {
    }

    public Restaurante(Long id, String nombre) {
        this.id = getId();
        this.nombre = nombre;
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

    public Map<Producto, Integer> getMenu() {
        return menu;
    }

    public void setMenu(Map<Producto, Integer> menu) {
        this.menu = menu;
    }

    public List<Resenia> getReseniasRestaurante() {
        return reseniasRestaurante;
    }

    public void setReseniasRestaurante(List<Resenia> reseniasRestaurante) {
        this.reseniasRestaurante = reseniasRestaurante;
    }
}
