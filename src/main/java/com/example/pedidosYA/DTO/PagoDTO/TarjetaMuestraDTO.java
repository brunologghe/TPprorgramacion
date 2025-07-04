package com.example.pedidosYA.DTO.PagoDTO;

import com.example.pedidosYA.Model.MetodoDePago;
import com.example.pedidosYA.Model.Tarjeta;

public record TarjetaMuestraDTO(Long id, MetodoDePago metodoDePago, String numero, String vencimiento) {
}
