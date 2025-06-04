package com.example.pedidosYA.DTO.PedidoDTO;

import java.time.LocalDateTime;

public record PedidoResumenDTO(
        Long id,
        LocalDateTime fecha,
        String estado,
        Double total
) {}
