package com.example.pedidosYA.Model;

import java.time.LocalDateTime;

public class Pedido {
    private Long id;
    private LocalDateTime fechaPedido;
    private double total;
    private EstadoPedido estado;
}
