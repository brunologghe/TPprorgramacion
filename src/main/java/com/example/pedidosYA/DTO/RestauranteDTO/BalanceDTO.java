
package com.example.pedidosYA.DTO.RestauranteDTO;

public record BalanceDTO(
        Double totalRecaudado,
        Integer cantidadPedidos,
        Double promedioVenta
) {}