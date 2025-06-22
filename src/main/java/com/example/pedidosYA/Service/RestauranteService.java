package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.ClienteDTO.ResponseDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.*;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.Restaurante;
import com.example.pedidosYA.Model.RolUsuario;
import com.example.pedidosYA.Model.Usuario;
import com.example.pedidosYA.Repository.RestauranteRepository;
import com.example.pedidosYA.Repository.UsuarioRepository;
import com.example.pedidosYA.Validations.RestauranteValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private RestauranteValidations restauranteValidations;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioRepository usuarioRepository;


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

    public RestauranteResponseDTO modificarRestaurante (String usuario, RestauranteModificarDTO restauranteNuevo){

        Long id = restauranteRepository.findByUsuario(usuario).get().getId();

        restauranteValidations.validarContraseniaActual(id, restauranteNuevo.getContraseniaActual());
        restauranteValidations.validarNombreNoDuplicadoConID(id, restauranteNuevo.getNombre());

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Restaurante no encontrado"));

        restaurante.setNombre(restauranteNuevo.getNombre());
        restaurante.setUsuario(restauranteNuevo.getUsuario());
        restaurante.setContrasenia(passwordEncoder.encode(restauranteNuevo.getContraseniaNueva()));

        Restaurante r = restauranteRepository.save(restaurante);

        return new RestauranteResponseDTO(r.getId(), r.getUsuario(), r.getNombre());
    }

    public RestauranteResponseDTO eliminarRestaurante (Long id){
        Restaurante restaurante = restauranteValidations.validarExisteId(id);

        RestauranteResponseDTO restauranteDTO = new RestauranteResponseDTO(
                restaurante.getId(),
                restaurante.getUsuario(),
                restaurante.getNombre()
        );

        restauranteRepository.delete(restaurante);
        return restauranteDTO;
    }

}
