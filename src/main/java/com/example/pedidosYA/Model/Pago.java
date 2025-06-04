package com.example.pedidosYA.Model;

import jakarta.persistence.*;

@Entity
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private MetodoDePago metodoDePago;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
