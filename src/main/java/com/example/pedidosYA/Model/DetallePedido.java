package com.example.pedidosYA.Model;

import java.util.Map;

public class DetallePedido {

    private Long id;
    private double subtotal;
    private Map<Producto, Integer> pedidoLista;
}
