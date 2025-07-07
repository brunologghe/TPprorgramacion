package com.example.pedidosYA.DTO.ProductoDTO;

import com.example.pedidosYA.Model.Restaurante;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;

public class ProductoCrearDTO {

    @NotBlank(message = "El nombre del producto es obligatorio.")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
    private String nombre;

    @NotBlank(message = "Las características del producto son obligatorias.")
    @Size(min = 5, max = 300, message = "Las características deben tener entre 5 y 300 caracteres.")
    private String caracteristicas;

    @Min(value = 0, message = "El precio mínimo debe ser 0")
    @Max(value = 400000, message = "El precio máximo debe ser 400000")
    private double precio;

    @Min(value = 0, message = "El stock no puede ser negativo.")
    @Max(value = 10000, message = "El stock no puede superar las 10.000 unidades.")
    private int stock;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }


    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }


    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

}
