package com.example.pedidosYA.DTO.RepartidorDTO;

import com.example.pedidosYA.Model.TipoVehiculo;

public record RepartidorDetailDTO(
        Long id,
        String usuario,
        String nombreYapellido,
        String email,
        String pais,
        TipoVehiculo tipoVehiculo,
        Boolean disponible,
        Boolean trabajando,
        Integer totalPedidosEntregados,
        Double calificacionPromedio
) {
}
