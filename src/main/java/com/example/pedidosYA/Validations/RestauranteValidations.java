package com.example.pedidosYA.Validations;

import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.Restaurante;
import com.example.pedidosYA.Model.Usuario;
import com.example.pedidosYA.Repository.RestauranteRepository;
import com.example.pedidosYA.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class RestauranteValidations {
    @Autowired
    RestauranteRepository restauranteRepository;

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public void validarNombreCrear(String nombre)throws BusinessException{
        Restaurante r = restauranteRepository.findByNombre(nombre);

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BusinessException("El nombre es obligatorio.");
        }
        if (nombre.length() > 25) {
            throw new BusinessException("El nombre no puede tener más de 25 caracteres.");
        }

        if (r != null && restauranteRepository.existsByNombre(nombre)) {
            throw new BusinessException("El nombre ya pertenece a otro restaurante.");
        }
    }

    public void validarNombreNoDuplicadoConID(Long id, String nombre)throws BusinessException{
        Restaurante r = restauranteRepository.findByNombre(nombre);

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BusinessException("El nombre es obligatorio.");
        }
        if (nombre.length() > 25) {
            throw new BusinessException("El nombre no puede tener más de 25 caracteres.");
        }
        if (r != null && restauranteRepository.existsByNombre(nombre) && r.getId() != id ) {
            throw new BusinessException("El nombre ya pertenece a otro restaurante.");
        }
    }


    public void validarContraseniaActual(Long id, String contrasenia){

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Restaurante no encontrado con id: " + id));

        if (!passwordEncoder.matches(contrasenia, restaurante.getContrasenia())) {
            throw new BusinessException("La contraseña actual es incorrecta.");
        }
    }

    public Restaurante validarExisteId(Long id){
        return restauranteRepository.findById(id).orElseThrow(()-> new BusinessException("No existe restaurante con este id"));
    }

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

    public void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("El email es obligatorio.");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new BusinessException("El email ya existe en el sistema.");
        }
    }

    public void validarEmailModificacion(Long id, String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("El email es obligatorio.");
        }
        Usuario usuarioConEmail = usuarioRepository.findByEmail(email).orElse(null);
        if (usuarioConEmail != null && !usuarioConEmail.getId().equals(id)) {
            throw new BusinessException("El email ya existe en el sistema.");
        }
    }

}
