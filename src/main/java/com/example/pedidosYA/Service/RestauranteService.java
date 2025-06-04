package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteCrearDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteDatailDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteResumenDTO;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.Restaurante;
import com.example.pedidosYA.Repository.RestauranteRepository;
import com.example.pedidosYA.Validations.RestauranteValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RestauranteService {

    @Autowired
    RestauranteRepository restauranteRepository;
    RestauranteValidations restauranteValidations;

    /*public RestauranteCrearDTO crearRestaurante(RestauranteCrearDTO restaurante){

        restauranteValidations.validarExisteNombre(restaurante.getNombre());
        restauranteRepository.save(restaurante);
        return
    }*/

    public RestauranteDatailDTO findRestaurante(String nombre){

        restauranteValidations.validarNoExisteNombre(nombre);

        Restaurante restaurante = restauranteRepository.findByNombre(nombre);
        return new RestauranteDatailDTO(restaurante.getId(), restaurante.getNombre(),
                restaurante.getMenu(), restaurante.getReseniasRestaurante());
    }

    public Set<RestauranteResumenDTO> findAllRestaurantes(){
        return restauranteRepository.findAll().stream().map(r -> new RestauranteResumenDTO(r.getId(), r.getNombre())).collect(Collectors.toSet());
    }
}
