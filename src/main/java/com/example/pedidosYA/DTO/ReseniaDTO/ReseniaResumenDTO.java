package com.example.pedidosYA.DTO.ReseniaDTO;

public record ReseniaResumenDTO(
        Long idCliente,
        String nombreCliente,
        String resenia,
        Double puntuacion
) {
}