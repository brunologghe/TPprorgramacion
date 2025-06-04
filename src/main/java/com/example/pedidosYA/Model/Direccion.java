package com.example.pedidosYA.Model;

import jakarta.persistence.*;

public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String direccion;
    private String ciudad;
    private String pais;
    private String codigoPostal;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

}
