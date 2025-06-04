package com.example.pedidosYA.DTO.ClienteDTO;

import jakarta.validation.constraints.NotBlank;

public class ClienteCrearDTO {
    @NotBlank
    private String usuario;

    @NotBlank
    private String contrasenia;

    @NotBlank
    private String nombreYapellido;

}
