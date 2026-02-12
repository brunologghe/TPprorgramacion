package com.example.pedidosYA.DTO.PedidoDTO;

import com.example.pedidosYA.Model.EstadoPedido;

import java.time.LocalDateTime;

public class PedidoRepartidorDTO {
    private Long id;
    private LocalDateTime fechaPedido;
    private double total;
    private EstadoPedido estado;
    
    // Datos mínimos del cliente
    private Long clienteId;
    private String clienteNombre;
    private String clienteTelefono;
    
    // Datos mínimos del restaurante
    private Long restauranteId;
    private String restauranteNombre;
    private String restauranteDireccion;
    
    // Dirección de entrega
    private String direccionEntrega;
    private String codigoPostal;

    public PedidoRepartidorDTO() {
    }

    public PedidoRepartidorDTO(Long id, LocalDateTime fechaPedido, double total, EstadoPedido estado, 
                               Long clienteId, String clienteNombre, String clienteTelefono,
                               Long restauranteId, String restauranteNombre, String restauranteDireccion,
                               String direccionEntrega, String codigoPostal) {
        this.id = id;
        this.fechaPedido = fechaPedido;
        this.total = total;
        this.estado = estado;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.clienteTelefono = clienteTelefono;
        this.restauranteId = restauranteId;
        this.restauranteNombre = restauranteNombre;
        this.restauranteDireccion = restauranteDireccion;
        this.direccionEntrega = direccionEntrega;
        this.codigoPostal = codigoPostal;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteTelefono() {
        return clienteTelefono;
    }

    public void setClienteTelefono(String clienteTelefono) {
        this.clienteTelefono = clienteTelefono;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public String getRestauranteNombre() {
        return restauranteNombre;
    }

    public void setRestauranteNombre(String restauranteNombre) {
        this.restauranteNombre = restauranteNombre;
    }

    public String getRestauranteDireccion() {
        return restauranteDireccion;
    }

    public void setRestauranteDireccion(String restauranteDireccion) {
        this.restauranteDireccion = restauranteDireccion;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
}
