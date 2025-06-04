package com.example.pedidosYA.DTO.RestauranteDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RestauranteCrearDTO {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{3,18}$", message = "El usuario debe tener entre 3 y 18 caracteres y solo puede contener letras o números")
    private String usuario;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._]{3,15}$", message = "La contrasenia debe tener entre 3 y 15 caracteres y solo puede contener letras, números, _ o .")
    private String contrasenia;

    @NotBlank
    @Size(max = 25)
    private String nombre;

}
