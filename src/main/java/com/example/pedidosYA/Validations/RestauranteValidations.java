package com.example.pedidosYA.Validations;

import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Repository.RestauranteRepository;


public class RestauranteValidations {

    RestauranteRepository restauranteRepository;
public void validarExisteNombre(String nombre)throws BusinessException{
    if(restauranteRepository.existsByNombre(nombre)){
        throw new BusinessException("El nombre de este restaurante ya existe");
    }
}

    public void validarNoExisteNombre(String nombre)throws BusinessException{
        if(!restauranteRepository.existsByNombre(nombre)){
            throw new BusinessException("El nombre de este restaurante no existe");
        }
    }

}
