package com.example.pedidosYA.Model;

import jakarta.persistence.*;

@Entity
public class ProductoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Producto producto;

    private Integer cantidad;

    @ManyToOne
    private DetallePedido detallePedido;
}
