package com.example.pedidosYA.DTO.RepartidorDTO;

import com.example.pedidosYA.Model.TipoVehiculo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ActualizarPerfilRepartidorDTO {
    
    @Size(min = 3, max = 100, message = "El nombre y apellido debe tener entre 3 y 100 caracteres")
    private String nombreYapellido;
    
    @Email(message = "El formato del email no es válido")
    private String email;
    
    private String pais;
    
    private TipoVehiculo tipoVehiculo;
    
    @NotBlank(message = "La contraseña actual es obligatoria para verificar la identidad")
    private String contraseniaActual;

    public String getNombreYapellido() {
        return nombreYapellido;
    }

    public void setNombreYapellido(String nombreYapellido) {
        this.nombreYapellido = nombreYapellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public String getContraseniaActual() {
        return contraseniaActual;
    }

    public void setContraseniaActual(String contraseniaActual) {
        this.contraseniaActual = contraseniaActual;
    }
}
