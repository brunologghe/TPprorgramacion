package com.example.pedidosYA.Validations;

import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.Restaurante;
import com.example.pedidosYA.Repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestauranteValidations {
    @Autowired
    RestauranteRepository restauranteRepository;


    public void validarNombreNoDuplicadoConID(Long id, String nombre)throws BusinessException{
        Restaurante r = restauranteRepository.findByNombre(nombre);
        if (r != null && restauranteRepository.existsByNombre(nombre) && r.getId() != id ) {
            throw new BusinessException("El nombre ya pertenece a otro restaurante.");
        }
    }

    public void validarNombreExistente(String nombre)throws BusinessException{
        if(!restauranteRepository.existsByNombre(nombre)){
            throw new BusinessException("El nombre de este restaurante no existe");
        }
    }

    public void validarContraseniaActual(Long id, String contrasenia){

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Restaurante no encontrado con id: " + id));

        if (!restaurante.getContrasenia().equals(contrasenia)) {
            throw new BusinessException("La contraseña actual es incorrecta.");
        }
    }

    public Restaurante validarExisteId(Long id){
        return restauranteRepository.findById(id).orElseThrow(()-> new BusinessException("No existe restaurante con este id"));
    }

}
