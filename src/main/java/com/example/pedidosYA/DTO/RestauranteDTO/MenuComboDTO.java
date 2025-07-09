package com.example.pedidosYA.DTO.RestauranteDTO;

import com.example.pedidosYA.DTO.ProductoDTO.ProductoResumenDTO;

import java.util.List;

public record MenuComboDTO(List<ProductoResumenDTO> menu, List<ComboResponseDTO>listaCombos) {
}
