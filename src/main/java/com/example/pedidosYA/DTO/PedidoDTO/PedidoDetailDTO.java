package com.example.pedidosYA.DTO.PedidoDTO;

import com.example.pedidosYA.DTO.RepartidorDTO.RepartidorResumenDTO;
import com.example.pedidosYA.Model.EstadoPedido;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoDetailDTO(
        Long id,
        LocalDateTime fecha,
        EstadoPedido estado,
        Double total,
        String nombreRestaurante,
        Long idCliente,
        List<DetallePedidoDTO> detalles,
        RepartidorResumenDTO repartidor
) {}
