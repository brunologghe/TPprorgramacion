package com.example.pedidosYA.DTO.DireccionDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DireccionEliminarDTO {

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "El código postal es obligatorio")
    private String codigoPostal;

    @NotNull(message = "El cliente es obligatorio")
    private Long id;

    public @NotBlank(message = "La dirección es obligatoria") String getDireccion() {
        return direccion;
    }

    public void setDireccion(@NotBlank(message = "La dirección es obligatoria") String direccion) {
        this.direccion = direccion;
    }

    public @NotBlank(message = "El código postal es obligatorio") String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(@NotBlank(message = "El código postal es obligatorio") String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public @NotNull(message = "El cliente es obligatorio") Long getId() {
        return id;
    }

    public void setId(@NotNull(message = "El cliente es obligatorio") Long id) {
        this.id = id;
    }
}
