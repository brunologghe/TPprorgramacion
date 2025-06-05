package com.example.pedidosYA.Validations;

import com.example.pedidosYA.Exceptions.BusinessException;

public class UsuarioValidations {

    public static void validarUsuario(String usuario){
        if(usuario.length()< 3 || usuario.length()>18){
            throw new BusinessException("El usuario debe tener entre 3 y 18 caracteres");
        }
    }
}
