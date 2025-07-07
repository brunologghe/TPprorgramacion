package com.example.pedidosYA.DTO.DireccionDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DireccionCrearDTO {
    @NotBlank(message = "La dirección es obligatoria")
    @Size(min = 5, max = 100, message = "La dirección debe tener entre 5 y 100 caracteres")
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(min = 2, max = 50, message = "La ciudad debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "La ciudad solo puede contener letras")
    private String ciudad;

    @NotBlank(message = "El país es obligatorio")
    @Size(min = 2, max = 50, message = "El país debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "El país solo puede contener letras")
    private String pais;

    @NotBlank(message = "El código postal es obligatorio")
    @Pattern(regexp = "\\d{4,10}", message = "El código postal debe contener entre 4 y 10 dígitos")
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
