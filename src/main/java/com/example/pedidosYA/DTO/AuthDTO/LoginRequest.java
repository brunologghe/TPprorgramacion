package com.example.pedidosYA.DTO.AuthDTO;

public class LoginRequest {
    private String usuario;
    private String contrasenia;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String username) {
        this.usuario = username;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
