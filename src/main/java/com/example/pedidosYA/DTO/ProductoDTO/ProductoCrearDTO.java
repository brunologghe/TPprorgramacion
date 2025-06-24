package com.example.pedidosYA.DTO.ProductoDTO;

import com.example.pedidosYA.Model.Restaurante;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductoCrearDTO {

    @NotBlank
    private String nombre;
    @NotBlank
    private String caracteristicas;
    @Min(value = 0, message = "El precio mínimo debe ser 0")
    @Max(value = 400000, message = "El precio máximo debe ser 400000")
    private double precio;
    @Min(value = 0, message = "El minimo de stock es 0")
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
