package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.RestauranteDTO.*;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.Restaurante;
import com.example.pedidosYA.Repository.RestauranteRepository;
import com.example.pedidosYA.Validations.RestauranteValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RestauranteService {

    @Autowired
    RestauranteRepository restauranteRepository;

    @Autowired
    RestauranteValidations restauranteValidations;

    public RestauranteResponseDTO crearRestaurante(RestauranteCrearDTO dto) {


        Restaurante restaurante = new Restaurante();
        Restaurante restAux = restauranteRepository.findByNombre(dto.getNombre());

        restauranteValidations.validarNombreNoDuplicado(restAux.getId(), dto.getNombre());

        restaurante.setNombre(dto.getNombre());
        restaurante.setUsuario(dto.getUsuario());
        restaurante.setContrasenia(dto.getContrasenia());

        Restaurante r = restauranteRepository.save(restaurante);

        return new RestauranteResponseDTO(r.getId(), r.getUsuario(), r.getNombre());
    }

    public RestauranteDetailDTO findRestauranteByNombre(String nombre){

        restauranteValidations.validarNombreExistente(nombre);

        Restaurante restaurante = restauranteRepository.findByNombre(nombre);
        return new RestauranteDetailDTO(restaurante.getId(), restaurante.getNombre(),
                restaurante.getMenu(), restaurante.getReseniasRestaurante());
    }

    public Set<RestauranteResumenDTO> findAllRestaurantes(){
        List<Restaurante> lista = restauranteRepository.findAll();
        if (lista.isEmpty()) {
            throw new BusinessException("No hay restaurantes cargados actualmente");
        }
        return restauranteRepository.findAll().stream().map(r -> new RestauranteResumenDTO(r.getId(), r.getNombre())).collect(Collectors.toSet());
    }

    public RestauranteResponseDTO modificarRestaurante (Long id, RestauranteModificarDTO restauranteNuevo){

        restauranteValidations.validarContraseniaActual(id, restauranteNuevo.getContraseniaActual());
        restauranteValidations.validarNombreNoDuplicado(id, restauranteNuevo.getNombre());
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Restaurante no encontrado"));

        restaurante.setNombre(restauranteNuevo.getNombre());
        restaurante.setUsuario(restauranteNuevo.getUsuario());
        restaurante.setContrasenia(restauranteNuevo.getContraseniaNueva());

        Restaurante r = restauranteRepository.save(restaurante);

        return new RestauranteResponseDTO(r.getId(), r.getUsuario(), r.getNombre());
    }

    public void eliminarRestaurante (Long id){
        Restaurante restaurante = restauranteValidations.validarExisteId(id);
        restauranteRepository.delete(restaurante);
    }
}
