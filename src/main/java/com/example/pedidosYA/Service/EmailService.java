package com.example.pedidosYA.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.enabled:true}")
    private boolean mailEnabled;

    @Async
    public void enviarEmailPedidoConfirmado(String emailCliente, Long idPedido, Double total) {
        if (!mailEnabled) {
            logger.info("Email disabled - skip sending pedido confirmado to {} for pedido {}", emailCliente, idPedido);
            return;
        }
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(emailCliente);
            mensaje.setSubject("Pedido #" + idPedido + " Confirmado - Quickish");
            mensaje.setText(
                    "¡Hola!\n\n" +
                            "Tu pedido #" + idPedido + " ha sido confirmado.\n" +
                            "Total: $" + String.format("%.2f", total) + "\n\n" +
                            "Te notificaremos cuando el restaurante lo prepare y envíe.\n\n" +
                            "Gracias por usar Quickish.\n\n" +
                            "---\n" +
                            "Este es un email automático, por favor no respondas."
            );
            mailSender.send(mensaje);
            logger.info("Enviado email confirmacion pedido {} a {}", idPedido, emailCliente);
        } catch (MailException e) {
            logger.error("Error al enviar email de confirmacion a {} para pedido {}: {}", emailCliente, idPedido, e.getMessage(), e);
        }
    }

    @Async
    public void enviarEmailCambioEstado(String emailCliente, Long idPedido, String nuevoEstado) {
        if (!mailEnabled) {
            logger.info("Email disabled - skip sending cambio estado to {} for pedido {}", emailCliente, idPedido);
            return;
        }
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(emailCliente);
            mensaje.setSubject("Actualización Pedido #" + idPedido + " - Quickish");

            String textoEstado = switch(nuevoEstado) {
                case "PREPARACION" -> "está siendo preparado por el restaurante";
                case "ENVIADO" -> "está en camino hacia tu dirección";
                case "CANCELADO" -> "ha sido cancelado";
                default -> "ha sido actualizado";
            };

            mensaje.setText(
                    "¡Hola!\n\n" +
                            "Tu pedido #" + idPedido + " " + textoEstado + ".\n\n" +
                            "Estado actual: " + nuevoEstado + "\n\n" +
                            "Gracias por usar Quickish.\n\n" +
                            "---\n" +
                            "Este es un email automático, por favor no respondas."
            );
            mailSender.send(mensaje);
            logger.info("Enviado email cambio estado pedido {} a {} (estado={})", idPedido, emailCliente, nuevoEstado);
        } catch (MailException e) {
            logger.error("Error al enviar email de cambio de estado a {} para pedido {}: {}", emailCliente, idPedido, e.getMessage(), e);
        }
    }

    @Async
    public void enviarEmailNuevoPedidoRestaurante(String emailRestaurante, Long idPedido, String nombreCliente, Double total) {
        if (!mailEnabled) {
            logger.info("Email disabled - skip sending nuevo pedido to {} for pedido {}", emailRestaurante, idPedido);
            return;
        }
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(emailRestaurante);
            mensaje.setSubject("¡Nuevo Pedido #" + idPedido + "! - Quickish");
            mensaje.setText(
                    "¡Tienes un nuevo pedido!\n\n" +
                            "Pedido #" + idPedido + "\n" +
                            "Cliente: " + nombreCliente + "\n" +
                            "Total: $" + String.format("%.2f", total) + "\n\n" +
                            "Ingresa a Quickish para ver los detalles y gestionar el pedido.\n\n" +
                            "---\n" +
                            "Este es un email automático, por favor no respondas."
            );
            mailSender.send(mensaje);
            logger.info("Enviado email nuevo pedido {} a restaurante {}", idPedido, emailRestaurante);
        } catch (MailException e) {
            logger.error("Error al enviar email nuevo pedido a {} para pedido {}: {}", emailRestaurante, idPedido, e.getMessage(), e);
        }
    }

    // Método privado preparado para enviar HTML si se necesita en el futuro
    private void enviarHtml(String to, String subject, String htmlBody) throws MessagingException {
        if (!mailEnabled) {
            logger.info("Email disabled - skip sending html email to {}", to);
            return;
        }
        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        mailSender.send(mime);
    }
}