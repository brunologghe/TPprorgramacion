package com.example.pedidosYA.DTO.PagoDTO;

import com.example.pedidosYA.Model.MetodoDePago;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TarjetaRequestDTO {
    @NotNull(message = "El tipo de tarjeta es obligatorio.")
    private MetodoDePago tipo;

    @NotBlank(message = "El número de tarjeta es obligatorio.")
    @Pattern(regexp = "\\d{16}", message = "El número debe tener 16 dígitos.")
    private String numero;

    @NotBlank(message = "El titular es obligatorio.")
    @Size(max = 100, message = "El titular no puede tener más de 100 caracteres.")
    private String titular;

    @NotBlank(message = "El vencimiento es obligatorio.")
    @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{2}", message = "El vencimiento debe ser en formato MM/YY.")
    private String vencimiento;

    @NotBlank(message = "El CVV es obligatorio.")
    @Pattern(regexp = "\\d{3}", message = "El CVV debe tener 3 dígitos.")
    private String cvv;

    public MetodoDePago getTipo() {
        return tipo;
    }

    public void setTipo(MetodoDePago tipo) {
        this.tipo = tipo;
    }

    public @NotBlank(message = "El número de tarjeta es obligatorio.") @Pattern(regexp = "\\d{16}", message = "El número debe tener 16 dígitos.") String getNumero() {
        return numero;
    }

    public void setNumero(@NotBlank(message = "El número de tarjeta es obligatorio.") @Pattern(regexp = "\\d{16}", message = "El número debe tener 16 dígitos.") String numero) {
        this.numero = numero;
    }

    public @NotBlank(message = "El titular es obligatorio.") @Size(max = 100, message = "El titular no puede tener más de 100 caracteres.") String getTitular() {
        return titular;
    }

    public void setTitular(@NotBlank(message = "El titular es obligatorio.") @Size(max = 100, message = "El titular no puede tener más de 100 caracteres.") String titular) {
        this.titular = titular;
    }

    public @NotBlank(message = "El vencimiento es obligatorio.") @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{2}", message = "El vencimiento debe ser en formato MM/YY.") String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(@NotBlank(message = "El vencimiento es obligatorio.") @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{2}", message = "El vencimiento debe ser en formato MM/YY.") String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public @NotBlank(message = "El CVV es obligatorio.") @Pattern(regexp = "\\d{3}", message = "El CVV debe tener 3 dígitos.") String getCvv() {
        return cvv;
    }

    public void setCvv(@NotBlank(message = "El CVV es obligatorio.") @Pattern(regexp = "\\d{3}", message = "El CVV debe tener 3 dígitos.") String cvv) {
        this.cvv = cvv;
    }
}
