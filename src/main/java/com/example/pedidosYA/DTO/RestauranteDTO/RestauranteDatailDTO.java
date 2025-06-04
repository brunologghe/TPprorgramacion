package com.example.pedidosYA.DTO.RestauranteDTO;

import com.example.pedidosYA.Model.Producto;
import com.example.pedidosYA.Model.Resenia;

import java.util.List;
import java.util.Map;

public record RestauranteDatailDTO (

        Long id,
        String nombre,
        Map<Producto, Integer> menu,
        List<Resenia> reseniasRestaurante
)
{}

