package com.example.pedidosYA.DTO.AdminDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AdminRequestDTO {

    @NotNull
    @NotBlank
    private String usuario;
    @NotNull
    @NotBlank
    private String contrasenia;

    public @NotNull @NotBlank String getUsuario() {
        return usuario;
    }

    public void setUsuario(@NotNull @NotBlank String usuario) {
        this.usuario = usuario;
    }

    public @NotNull @NotBlank String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(@NotNull @NotBlank String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
