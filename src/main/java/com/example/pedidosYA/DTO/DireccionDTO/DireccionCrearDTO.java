package com.example.pedidosYA.DTO.DireccionDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DireccionCrearDTO {
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotBlank(message = "El país es obligatorio")
    private String pais;

    @NotBlank(message = "El código postal es obligatorio")
    private String codigoPostal;

    public @NotBlank(message = "La dirección es obligatoria") String getDireccion() {
        return direccion;
    }

    public void setDireccion(@NotBlank(message = "La dirección es obligatoria") String direccion) {
        this.direccion = direccion;
    }

    public @NotBlank(message = "La ciudad es obligatoria") String getCiudad() {
        return ciudad;
    }

    public void setCiudad(@NotBlank(message = "La ciudad es obligatoria") String ciudad) {
        this.ciudad = ciudad;
    }

    public @NotBlank(message = "El país es obligatorio") String getPais() {
        return pais;
    }

    public void setPais(@NotBlank(message = "El país es obligatorio") String pais) {
        this.pais = pais;
    }

    public @NotBlank(message = "El código postal es obligatorio") String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(@NotBlank(message = "El código postal es obligatorio") String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
}
