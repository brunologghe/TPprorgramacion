package com.example.pedidosYA.DTO.DireccionDTO;

import jakarta.validation.constraints.NotBlank;

public class DireccionCrearDTO {
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotBlank(message = "El país es obligatorio")
    private String pais;

    @NotBlank(message = "El código postal es obligatorio")
    private String codigoPostal;
}
