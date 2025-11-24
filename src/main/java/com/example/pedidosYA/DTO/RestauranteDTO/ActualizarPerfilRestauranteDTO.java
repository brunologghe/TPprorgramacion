package com.example.pedidosYA.DTO.RestauranteDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public class ActualizarPerfilRestauranteDTO {
    
    @Size(min = 3, max = 25, message = "El nombre del restaurante debe tener entre 3 y 25 caracteres")
    private String nombreRestaurante;
    
    @Email(message = "El formato del email no es válido")
    private String email;
    
    @NotBlank(message = "La contraseña actual es obligatoria para verificar la identidad")
    private String contraseniaActual;

    private LocalTime horaApertura;

    private LocalTime horaCierre;

    public LocalTime getHoraApertura() {
        return horaApertura;
    }

    public void setHoraApertura(LocalTime horaApertura) {
        this.horaApertura = horaApertura;
    }

    public LocalTime getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(LocalTime horaCierre) {
        this.horaCierre = horaCierre;
    }

    public String getNombreRestaurante() {
        return nombreRestaurante;
    }

    public void setNombreRestaurante(String nombreRestaurante) {
        this.nombreRestaurante = nombreRestaurante;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseniaActual() {
        return contraseniaActual;
    }

    public void setContraseniaActual(String contraseniaActual) {
        this.contraseniaActual = contraseniaActual;
    }
}