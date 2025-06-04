package com.example.pedidosYA.Model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

public class Pedido {
    private Long id;
    private LocalDateTime fechaPedido;
    private double total;
    private EstadoPedido estado;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
