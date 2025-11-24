package com.example.pedidosYA.DTO.RestauranteDTO;

import com.example.pedidosYA.Model.EstadoRestaurante;

import java.time.LocalDateTime;

public record RestauranteEstadoDTO(
        Long id,
        String usuario,
        String nombre,
        EstadoRestaurante estado,
        String motivoRechazo
) {}