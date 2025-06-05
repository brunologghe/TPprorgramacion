package com.example.pedidosYA.DTO.ProductoDTO;

import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteResumenDTO;

public record ProductoResumenDTO(

        Long id,
        String nombre,
        double precio
)
{}
