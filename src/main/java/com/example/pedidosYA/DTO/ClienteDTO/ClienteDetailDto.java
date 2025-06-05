package com.example.pedidosYA.DTO.ClienteDTO;

import com.example.pedidosYA.Model.Direccion;
import com.example.pedidosYA.Model.Pago;

import java.util.List;

public record ClienteDetailDto(Long id,
                               String usuario,
                               String nombreYapellido,
List<Direccion>direcciones, List<Pago>metodosPago) {
}
