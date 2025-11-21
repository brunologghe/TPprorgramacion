package com.example.pedidosYA.DTO.RestauranteDTO;

import jakarta.validation.constraints.*;

public class RestauranteCrearDTO {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{3,18}$", message = "El usuario debe tener entre 3 y 18 caracteres y solo puede contener letras o números")
    private String usuario;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._]{3,15}$", message = "La contrasenia debe tener entre 3 y 15 caracteres y solo puede contener letras, números, _ o .")
    private String contrasenia;

    @NotBlank
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank
    @Size(max = 25)
    private String nombre;

    public  String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
