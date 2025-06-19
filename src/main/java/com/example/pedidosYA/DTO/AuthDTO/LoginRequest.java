package com.example.pedidosYA.DTO.AuthDTO;

public class LoginRequest {
    private String usuario;
    private String contrasenia;

    public String getUsername() {
        return usuario;
    }

    public void setUsername(String username) {
        this.usuario = username;
    }

    public String getPassword() {
        return contrasenia;
    }

    public void setPassword(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
