package com.example.pedidosYA.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Tarjeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El tipo de tarjeta es obligatorio.")
    private MetodoDePago tipo;  // Enum: CREDITO o DEBITO

    @Column(length = 16)
    @NotBlank(message = "El número de tarjeta es obligatorio.")
    private String numero;

    @NotBlank(message = "El titular es obligatorio.")
    private String titular;

    @NotBlank(message = "La fecha de vencimiento es obligatoria.")
    private String vencimiento;  // Formato MM/YY

    @NotBlank(message = "El CVV es obligatorio.")
    @Column(length = 3)
    private String cvv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public Tarjeta() {}

    public Tarjeta(MetodoDePago tipo, String numero, String titular, String vencimiento, String cvv, Cliente cliente) {
        this.tipo = tipo;
        this.numero = numero;
        this.titular = titular;
        this.vencimiento = vencimiento;
        this.cvv = cvv;
        this.cliente = cliente;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public MetodoDePago getTipo() {
        return tipo;
    }

    public void setTipo(MetodoDePago tipo) {
        this.tipo = tipo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getNumeroEnmascarado() {
        if (numero == null || numero.length() < 4) return "****";
        return "**** **** **** " + numero.substring(numero.length() - 4);
    }
}
