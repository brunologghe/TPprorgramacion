package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.DireccionDTO.DireccionCrearDTO;
import com.example.pedidosYA.DTO.DireccionDTO.DireccionDTO;
import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Direccion;
import com.example.pedidosYA.Repository.ClienteRepository;
import com.example.pedidosYA.Repository.DireccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DireccionService {

    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    public DireccionDTO crearDireccion(DireccionCrearDTO direccion){
        Optional<Cliente>clienteOptional = clienteRepository.findById(direccion.getId());

        if (clienteOptional.isEmpty()) {
            throw new RuntimeException("Cliente no encontrado con ID: " + direccion.getId());
        }

        Cliente cliente = clienteOptional.get();
        Direccion direccionNueva = new Direccion();
        direccionNueva.setDireccion(direccion.getDireccion());
        direccionNueva.setCiudad(direccion.getCiudad());
        direccionNueva.setPais(direccion.getPais());
        direccionNueva.setCliente(cliente);
        direccionNueva.setCodigoPostal(direccion.getCodigoPostal());

        Direccion direccionRetorno = direccionRepository.save(direccionNueva);

        return new DireccionDTO(direccionRetorno.getId(), direccionRetorno.getDireccion(), direccionRetorno.getCiudad(), direccionRetorno.getPais(), direccionRetorno.getCodigoPostal());
    }


}
