package com.example.pedidosYA.DTO.ReseniaDTO;

import com.example.pedidosYA.DTO.PedidoDTO.DetallePedidoDTO;
import jakarta.validation.constraints.*;

import java.util.List;

public class ReseniaCreateDTO {

    @NotNull(message = "El restaurante no puede ser nulo")
    @Min(value = 1, message = "El restauranteId debe ser mayor o igual a 1")
    private Long restauranteId;

    @NotBlank(message = "La reseña no puede ser nula ni vacía")
    @Size(min = 10, max = 500, message = "La reseña debe tener entre 10 y 500 caracteres")
    private String resenia;
    @NotNull(message = "La puntuacion no puede ser nula")
    @DecimalMin(value = "0.1", inclusive = true, message = "La puntuación debe ser al menos 0.1")
    @DecimalMax(value = "5.0", inclusive = true, message = "La puntuación no puede ser mayor a 5")
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
