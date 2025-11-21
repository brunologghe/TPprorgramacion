package com.example.pedidosYA.DTO.ClienteDTO;

import com.example.pedidosYA.Model.Direccion;
import com.example.pedidosYA.Model.Tarjeta;

import java.util.List;

public record ClienteDetailDto(Long id,
                               String usuario,
                               String nombreYapellido,
                               String email,
List<Direccion>direcciones, List<Tarjeta>metodosPago) {
}
