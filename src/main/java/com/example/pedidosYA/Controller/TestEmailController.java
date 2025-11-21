package com.example.pedidosYA.Controller;

import com.example.pedidosYA.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestEmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/email")
    public String sendTestEmail(@RequestParam String to, @RequestParam(defaultValue = "confirmacion") String type) {
        if (type.equalsIgnoreCase("confirmacion")) {
            emailService.enviarEmailPedidoConfirmado(to, 123L, 456.78);
            return "Enviado email de confirmacion (async) a " + to;
        } else if (type.equalsIgnoreCase("nuevo-rest")) {
            emailService.enviarEmailNuevoPedidoRestaurante(to, 222L, "Cliente Demo", 99.99);
            return "Enviado email nuevo-pedido a restaurante " + to;
        } else if (type.equalsIgnoreCase("cambio")) {
            emailService.enviarEmailCambioEstado(to, 333L, "ENVIADO");
            return "Enviado email cambio-estado a " + to;
        }
        return "Tipo no soportado";
    }
}

