package com.example.pedidosYA.Model;

import jakarta.persistence.*;

@Entity
public class Resenia {
    @Id
    private Long id;
    @Column
    private int puntuacion;
    @Column
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;
}
