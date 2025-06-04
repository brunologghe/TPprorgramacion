package com.example.pedidosYA.DTO.RestauranteDTO;

import com.example.pedidosYA.Model.Producto;
import com.example.pedidosYA.Model.Resenia;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record RestauranteDatailDTO (

        Long id,
        String nombre,
        Set<Producto> menu,
        List<Resenia> reseniasRestaurante
)
{}

