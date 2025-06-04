package com.example.pedidosYA.Model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
public class Restaurante extends Usuario{

    @Column
    private String nombre;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Producto> menu;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resenia> reseniasRestaurante;

    public Restaurante() {
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
}
