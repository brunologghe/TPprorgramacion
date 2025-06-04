package com.example.pedidosYA.Model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class Pago {
    private Long id;
    private MetodoDePago metodoDePago;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
