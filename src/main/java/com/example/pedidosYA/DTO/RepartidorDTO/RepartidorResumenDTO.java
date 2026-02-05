package com.example.pedidosYA.DTO.RepartidorDTO;

import com.example.pedidosYA.Model.TipoVehiculo;

public record RepartidorResumenDTO(
        Long id,
        String nombreYapellido,
        String email,
        String pais,
        TipoVehiculo tipoVehiculo
) {
}
