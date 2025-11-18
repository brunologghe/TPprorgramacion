package com.example.pedidosYA.DTO.RestauranteDTO;

import com.example.pedidosYA.DTO.DireccionDTO.DireccionDTO;
import com.example.pedidosYA.DTO.ProductoDTO.ProductoResumenDTO;
import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaResumenDTO;
import com.example.pedidosYA.Model.Direccion;
import com.example.pedidosYA.Model.Producto;
import com.example.pedidosYA.Model.Resenia;

import java.util.List;
import java.util.Set;

public record RestauranteDetailDTO(

        Long id,
        String nombre,
        String email,
        Set<ProductoResumenDTO> menu,
        List<ReseniaResumenDTO> reseniasRestaurante,
        List<DireccionDTO>direcciones
)
{}

