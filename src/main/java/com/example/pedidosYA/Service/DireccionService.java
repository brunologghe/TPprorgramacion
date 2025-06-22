package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.DireccionDTO.DireccionCrearDTO;
import com.example.pedidosYA.DTO.DireccionDTO.DireccionDTO;
import com.example.pedidosYA.DTO.DireccionDTO.DireccionEliminarDTO;
import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Direccion;
import com.example.pedidosYA.Repository.ClienteRepository;
import com.example.pedidosYA.Repository.DireccionRepository;
import com.example.pedidosYA.Validations.ClienteValidations;
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
    private ClienteValidations clienteValidations;

    public DireccionDTO crearDireccion(String username, DireccionCrearDTO direccion) {
        Cliente cliente = clienteRepository.findByUsuario(username);

        Direccion nueva = new Direccion();
        nueva.setDireccion(direccion.getDireccion());
        nueva.setCiudad(direccion.getCiudad());
        nueva.setPais(direccion.getPais());
        nueva.setCodigoPostal(direccion.getCodigoPostal());
        nueva.setCliente(cliente);

        Direccion guardada = direccionRepository.save(nueva);

        return new DireccionDTO(guardada.getId(), guardada.getDireccion(), guardada.getCiudad(), guardada.getPais(), guardada.getCodigoPostal());
    }


    public void eliminarDireccion(String username, DireccionEliminarDTO dto) {
        Cliente cliente = clienteRepository.findByUsuario(username);
        Direccion direccion = direccionRepository.findByClienteIdAndCodigoPostalAndDireccion(
                cliente.getId(), dto.getCodigoPostal(), dto.getDireccion());

        if (direccion == null) {
            throw new RuntimeException("No se encontró la dirección deseada");
        }

        direccionRepository.delete(direccion);
    }

    public DireccionDTO modificarDireccion(String username, Long id, DireccionCrearDTO dto) {
        Cliente cliente = clienteRepository.findByUsuario(username);
        Direccion direccion = direccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró esa dirección"));

        if (!direccion.getCliente().getId().equals(cliente.getId())) {
            throw new RuntimeException("Esa dirección no pertenece al cliente logueado");
        }

        direccion.setDireccion(dto.getDireccion());
        direccion.setCiudad(dto.getCiudad());
        direccion.setPais(dto.getPais());
        direccion.setCodigoPostal(dto.getCodigoPostal());

        Direccion guardada = direccionRepository.save(direccion);
        return new DireccionDTO(guardada.getId(), guardada.getDireccion(), guardada.getCiudad(), guardada.getPais(), guardada.getCodigoPostal());
    }

    public List<DireccionDTO> listarDirecciones(String username) {
        Cliente cliente = clienteRepository.findByUsuario(username);
        List<Direccion> direcciones = direccionRepository.findByClienteId(cliente.getId());

        if (direcciones == null || direcciones.isEmpty()) {
            throw new RuntimeException("No hay direcciones aún para este cliente");
        }

        return direcciones.stream()
                .map(d -> new DireccionDTO(d.getId(), d.getDireccion(), d.getCiudad(), d.getPais(), d.getCodigoPostal()))
                .collect(Collectors.toList());
    }

}
