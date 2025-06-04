package com.example.pedidosYA.DTO.PedidoDTO;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoDetailDTO(
        Long id,
        LocalDateTime fecha,
        String estado,
        Double total,
        String nombreRestaurante,
        List<DetallePedidoDTO> detalles
) {}
