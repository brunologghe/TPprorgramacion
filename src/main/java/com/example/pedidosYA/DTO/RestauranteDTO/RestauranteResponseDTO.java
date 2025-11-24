package com.example.pedidosYA.DTO.RestauranteDTO;

public record RestauranteResponseDTO (

        Long id,
        String usuario,
        String nombre,
        String email,
        String horaApertura,   // "HH:mm"
        String horaCierre,     // "HH:mm"
        Boolean estaAbierto
)
{}
