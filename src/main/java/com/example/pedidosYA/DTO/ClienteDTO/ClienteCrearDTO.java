package com.example.pedidosYA.DTO.ClienteDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ClienteCrearDTO {
    @NotBlank
    private String usuario;

    @NotBlank
    private String contrasenia;

    @NotBlank
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank
    private String nombreYapellido;

    public @NotBlank String getUsuario() {
        return usuario;
    }

    public void setUsuario(@NotBlank String usuario) {
        this.usuario = usuario;
    }

    public @NotBlank String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(@NotBlank String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public @NotBlank @Email(message = "El formato del email no es válido") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email(message = "El formato del email no es válido") String email) {
        this.email = email;
    }

    public @NotBlank String getNombreYapellido() {
        return nombreYapellido;
    }

    public void setNombreYapellido(@NotBlank String nombreYapellido) {
        this.nombreYapellido = nombreYapellido;
    }
}
