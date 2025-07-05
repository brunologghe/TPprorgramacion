package com.example.pedidosYA.DTO.RestauranteDTO;

import com.example.pedidosYA.Model.Producto;
import com.example.pedidosYA.Model.Restaurante;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComboRequestDTO {

    @NotBlank(message = "El nombre del combo es obligatorio.")
    private String nombre;

    @Min(value = 0, message = "El precio mínimo debe ser 0")
    @Max(value = 400000, message = "El precio máximo debe ser 400000")
    private double descuento;

    @NotEmpty(message = "Debe seleccionar al menos un producto para el combo.")
    private Set<Long> productoIds;

    public @NotBlank(message = "El nombre del combo es obligatorio.") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "El nombre del combo es obligatorio.") String nombre) {
        this.nombre = nombre;
    }

    @Min(value = 0, message = "El precio mínimo debe ser 0")
    @Max(value = 400000, message = "El precio máximo debe ser 400000")
    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(@Min(value = 0, message = "El precio mínimo debe ser 0") @Max(value = 400000, message = "El precio máximo debe ser 400000") double descuento) {
        this.descuento = descuento;
    }

    public @NotEmpty(message = "Debe seleccionar al menos un producto para el combo.") Set<Long> getProductoIds() {
        return productoIds;
    }

    public void setProductoIds(@NotEmpty(message = "Debe seleccionar al menos un producto para el combo.") Set<Long> productoIds) {
        this.productoIds = productoIds;
    }
}
