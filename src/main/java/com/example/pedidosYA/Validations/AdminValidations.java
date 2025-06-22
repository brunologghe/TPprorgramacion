package com.example.pedidosYA.Validations;

import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminValidations {

    @Autowired
    UsuarioRepository usuarioRepository;
    public void validarUsuario(String usuario) {


        if (usuario == null || usuario.trim().isEmpty()) {
            throw new BusinessException("El usuario es obligatorio.");
        }
        if (!usuario.matches("^[a-zA-Z0-9]{3,18}$")) {
            throw new BusinessException("El usuario debe tener entre 3 y 18 caracteres y solo puede contener letras o números.");
        }

        if (usuarioRepository.existsByUsuario(usuario)) {
            throw new BusinessException("El usuario ya existe en el sistema.");
        }
    }

    public void validarContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.trim().isEmpty()) {
            throw new BusinessException("La contraseña es obligatoria.");
        }
        if (!contrasenia.matches("^[a-zA-Z0-9._]{3,15}$")) {
            throw new BusinessException("La contraseña debe tener entre 3 y 15 caracteres y solo puede contener letras, números, _ o .");
        }
    }
}
