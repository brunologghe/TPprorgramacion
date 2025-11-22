package com.example.pedidosYA.DTO.ProductoDTO;

import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteResumenDTO;

public record ProductoDetailDTO(

        Long id,
        String nombre,
        String caracteristicas,
        double precio,
        int stock,
        RestauranteResumenDTO restauranteResumen
        //hola

)
{}