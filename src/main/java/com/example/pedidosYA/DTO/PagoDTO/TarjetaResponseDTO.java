package com.example.pedidosYA.DTO.PagoDTO;

import com.example.pedidosYA.Model.MetodoDePago;

public class TarjetaResponseDTO {
    private Long id;
    private MetodoDePago tipo;
    private String numero;
    private String titular;
    private String vencimiento;
    private String cvv;

    public TarjetaResponseDTO() {}

    public TarjetaResponseDTO(Long id, MetodoDePago tipo, String numero, String titular, String vencimiento, String cvv) {
        this.id = id;
        this.tipo = tipo;
        this.numero = numero;
        this.titular = titular;
        this.vencimiento = vencimiento;
        this.cvv = cvv;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MetodoDePago getTipo() { return tipo; }
    public void setTipo(MetodoDePago tipo) { this.tipo = tipo; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }

    public String getVencimiento() { return vencimiento; }
    public void setVencimiento(String vencimiento) { this.vencimiento = vencimiento; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }
}