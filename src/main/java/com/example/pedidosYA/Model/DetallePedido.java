package com.example.pedidosYA.Model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Map;

@Entity
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double subtotal;

    @OneToMany(mappedBy = "detallePedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoPedido> pedidoLista;
}
