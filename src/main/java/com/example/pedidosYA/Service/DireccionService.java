package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.DireccionDTO.DireccionCrearDTO;
import com.example.pedidosYA.DTO.DireccionDTO.DireccionDTO;
import com.example.pedidosYA.DTO.DireccionDTO.DireccionEliminarDTO;
import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Direccion;
import com.example.pedidosYA.Repository.ClienteRepository;
import com.example.pedidosYA.Repository.DireccionRepository;
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

    public void eliminarDireccion(DireccionEliminarDTO direccion){
        Direccion direccionRetorno = direccionRepository.findByClienteIdAndCodigoPostalAndDireccion(direccion.getId(), direccion.getCodigoPostal(), direccion.getDireccion());

        if(direccionRetorno == null)
        {
            throw new RuntimeException("No se encontro la direccion deseada");
        }
        else
        {
            direccionRepository.delete(direccionRetorno);
        }
    }

    public DireccionDTO modificarDireccion(Long id, DireccionCrearDTO direccion){

        Direccion direRetorno = direccionRepository.findById(id).orElseThrow(()-> new RuntimeException("No se encontro esa direccion"));

        direRetorno.setCodigoPostal(direccion.getCodigoPostal());
        direRetorno.setDireccion(direccion.getDireccion());
        direRetorno.setPais(direccion.getPais());
        direRetorno.setCiudad(direccion.getCiudad());

        Cliente cliente = clienteRepository.findById(direccion.getId()).orElseThrow(()-> new RuntimeException("No existe un cliente con ese id"));
        direRetorno.setCliente(cliente);

        Direccion direccionRetorno = direccionRepository.save(direRetorno);

        return new DireccionDTO(direccionRetorno.getId(), direccionRetorno.getDireccion(), direccionRetorno.getCiudad(), direccionRetorno.getPais(), direccionRetorno.getCodigoPostal());
    }

    public List<DireccionDTO> listarDirecciones(Long id)
    {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(()-> new RuntimeException("No existe un cliente con ese id"));
        List<Direccion> direcciones = direccionRepository.findByClienteId(id);

        if(direcciones == null)
        {
            throw new RuntimeException("No hay direcciones aun en ese cliente");
        }

        return direcciones.stream().map(d -> new DireccionDTO(d.getId(), d.getDireccion(), d.getCiudad(), d.getPais(), d.getCodigoPostal())).collect(Collectors.toList());
    }
}
