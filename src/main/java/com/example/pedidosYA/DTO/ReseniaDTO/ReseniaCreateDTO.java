package com.example.pedidosYA.DTO.ReseniaDTO;

import com.example.pedidosYA.DTO.PedidoDTO.DetallePedidoDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ReseniaCreateDTO {
    @NotNull(message = "El restaurante no puede ser nulo")
    private Long restauranteId;
    @NotBlank(message = "La resenia no puede ser nula")
    private String resenia;
    @NotNull(message = "La puntuacion no puede ser nula")
    private Double puntuacion;


    public ReseniaCreateDTO() {
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public String getResenia() {
        return resenia;
    }

    public void setResenia(String resenia) {
        this.resenia = resenia;
    }

    public Double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Double puntuacion) {
        this.puntuacion = puntuacion;
    }
}
