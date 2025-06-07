package com.example.pedidosYA.DTO.ProductoDTO;

import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteResumenDTO;

public record ProductoModificarDTO (

        String nombre,
        String caracteristicas,
        double precio,
        int stock


)
{}
