package com.example.pedidosYA.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaPedido;
    private double total;
    private EstadoPedido estado;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
