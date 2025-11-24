package com.example.pedidosYA.DTO.RestauranteDTO;

public record EstadoRestauranteDTO(   // NUEVO
    boolean estaAbierto,
    String horaApertura,
    String horaCierre
) {}
