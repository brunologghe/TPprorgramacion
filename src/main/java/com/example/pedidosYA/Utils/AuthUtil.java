package com.example.pedidosYA.Utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    public static String getUsuarioLogueado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            String usuario = authentication.getPrincipal().toString();
            System.out.println("👤 [AuthUtil] Usuario logueado: " + usuario);
            return usuario;
        }
        
        System.out.println("❌ [AuthUtil] No hay usuario autenticado");
        throw new RuntimeException("No hay usuario autenticado. Necesita iniciar sesión.");
    }
}