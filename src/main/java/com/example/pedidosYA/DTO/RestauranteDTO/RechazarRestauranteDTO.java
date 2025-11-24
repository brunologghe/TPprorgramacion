package com.example.pedidosYA.DTO.RestauranteDTO;

import jakarta.validation.constraints.NotBlank;

public record RechazarRestauranteDTO(
        @NotBlank(message = "Debe proporcionar un motivo")
        String motivoRechazo
) {}
