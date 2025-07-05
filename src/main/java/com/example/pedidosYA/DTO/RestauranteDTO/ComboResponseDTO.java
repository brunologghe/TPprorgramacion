package com.example.pedidosYA.DTO.RestauranteDTO;

import com.example.pedidosYA.DTO.ProductoDTO.ProductoResumenDTO;

import java.util.List;
import java.util.Set;

public record ComboResponseDTO(String nombre, Set<ProductoResumenDTO> listaProductos, Double descuento, Double precioFinal) {
}
