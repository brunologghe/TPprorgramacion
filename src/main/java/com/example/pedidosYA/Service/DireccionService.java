package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.DireccionDTO.DireccionCrearDTO;
import com.example.pedidosYA.DTO.DireccionDTO.DireccionDTO;
import com.example.pedidosYA.DTO.DireccionDTO.DireccionEliminarDTO;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Direccion;
import com.example.pedidosYA.Model.Restaurante;
import com.example.pedidosYA.Repository.ClienteRepository;
import com.example.pedidosYA.Repository.DireccionRepository;
import com.example.pedidosYA.Repository.RestauranteRepository;
import com.example.pedidosYA.Validations.ClienteValidations;
import com.example.pedidosYA.Validations.DireccionValidations;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DireccionService {

    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private ClienteValidations clienteValidations;
    @Autowired
    private DireccionValidations direccionValidations;

    @Transactional
    public DireccionDTO crearDireccion(String username, DireccionCrearDTO direccion) {
        Optional<Cliente> clienteOpt = clienteRepository.findByUsuario(username);
        Optional<Restaurante> restauranteOpt = restauranteRepository.findByUsuario(username);

        Direccion nueva = new Direccion();
        nueva.setDireccion(direccion.getDireccion());
        nueva.setCiudad(direccion.getCiudad());
        nueva.setPais(direccion.getPais());
        nueva.setCodigoPostal(direccion.getCodigoPostal());

        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            nueva.setCliente(cliente);
            
            // Validar que no exista una dirección duplicada
            direccionValidations.validarDireccionDuplicadaPorCliente(cliente, nueva);
        } else if (restauranteOpt.isPresent()) {
            Restaurante restaurante = restauranteOpt.get();
            nueva.setRestaurante(restaurante);
            
            // Validar que no exista una dirección duplicada
            direccionValidations.validarDireccionDuplicadaPorRestaurante(restaurante, nueva);
        } else {
            throw new BusinessException("Usuario no encontrado");
        }

        Direccion guardada = direccionRepository.save(nueva);

        return new DireccionDTO(guardada.getId(), guardada.getDireccion(), guardada.getCiudad(), guardada.getPais(), guardada.getCodigoPostal());
    }

    @Transactional
    public void eliminarDireccion(String username, DireccionEliminarDTO dto) {
        Optional<Cliente> clienteOpt = clienteRepository.findByUsuario(username);
        Optional<Restaurante> restauranteOpt = restauranteRepository.findByUsuario(username);

        Direccion direccion = null;

        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            direccion = direccionRepository.findFirstByClienteIdAndCodigoPostalAndDireccion(
                    cliente.getId(), dto.getCodigoPostal(), dto.getDireccion());
        } else if (restauranteOpt.isPresent()) {
            Restaurante restaurante = restauranteOpt.get();
            direccion = direccionRepository.findFirstByRestauranteIdAndCodigoPostalAndDireccion(
                    restaurante.getId(), dto.getCodigoPostal(), dto.getDireccion());
        } else {
            throw new BusinessException("Usuario no encontrado");
        }

        if (direccion == null) {
            throw new BusinessException("No se encontró la dirección deseada");
        }

        direccionRepository.delete(direccion);
    }

    @Transactional
    public DireccionDTO modificarDireccion(String username, Long id, DireccionCrearDTO dto) {
        Optional<Cliente> clienteOpt = clienteRepository.findByUsuario(username);
        Optional<Restaurante> restauranteOpt = restauranteRepository.findByUsuario(username);

        Direccion direccion = direccionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("No se encontró esa dirección"));

        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            if (direccion.getCliente() == null || !direccion.getCliente().getId().equals(cliente.getId())) {
                throw new BusinessException("Esa dirección no pertenece al cliente logueado");
            }

            direccion.setDireccion(dto.getDireccion());
            direccion.setCiudad(dto.getCiudad());
            direccion.setPais(dto.getPais());
            direccion.setCodigoPostal(dto.getCodigoPostal());

            direccionValidations.validarDireccionDuplicadaPorCliente(cliente, direccion);

        } else if (restauranteOpt.isPresent()) {
            Restaurante restaurante = restauranteOpt.get();
            if (direccion.getRestaurante() == null || !direccion.getRestaurante().getId().equals(restaurante.getId())) {
                throw new BusinessException("Esa dirección no pertenece al restaurante logueado");
            }
            
            direccion.setDireccion(dto.getDireccion());
            direccion.setCiudad(dto.getCiudad());
            direccion.setPais(dto.getPais());
            direccion.setCodigoPostal(dto.getCodigoPostal());

            direccionValidations.validarDireccionDuplicadaPorRestaurante(restaurante, direccion);

        } else {
            throw new BusinessException("Usuario no encontrado");
        }

        Direccion guardada = direccionRepository.save(direccion);
        return new DireccionDTO(guardada.getId(), guardada.getDireccion(), guardada.getCiudad(), guardada.getPais(), guardada.getCodigoPostal());
    }


    public List<DireccionDTO> listarDirecciones(String username) {
        Optional<Cliente> clienteOpt = clienteRepository.findByUsuario(username);
        Optional<Restaurante> restauranteOpt = restauranteRepository.findByUsuario(username);

        List<Direccion> direcciones;

        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            direcciones = direccionRepository.findByClienteId(cliente.getId());

            direccionValidations.validarDirecciones(direcciones);

        } else if (restauranteOpt.isPresent()) {
            Restaurante restaurante = restauranteOpt.get();
            direcciones = direccionRepository.findByRestauranteId(restaurante.getId());
        } else {
            throw new BusinessException("Usuario no encontrado");
        }

        return direcciones.stream()
                .map(d -> new DireccionDTO(d.getId(), d.getDireccion(), d.getCiudad(), d.getPais(), d.getCodigoPostal()))
                .collect(Collectors.toList());
    }

}
