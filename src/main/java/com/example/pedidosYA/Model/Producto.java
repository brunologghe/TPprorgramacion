package com.example.pedidosYA.Model;

import jakarta.persistence.*;

@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;
    private String nombre;
    private String caracteristicas;
    private double precio;
    private int stock;
}
