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

    public DireccionDTO crearDireccion(DireccionCrearDTO direccion){
        Cliente cliente = clienteValidations.validarExistencia(direccion.getId());
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

        Cliente cliente = clienteValidations.validarExistencia(direccion.getId());
        direRetorno.setCliente(cliente);

        Direccion direccionRetorno = direccionRepository.save(direRetorno);

        return new DireccionDTO(direccionRetorno.getId(), direccionRetorno.getDireccion(), direccionRetorno.getCiudad(), direccionRetorno.getPais(), direccionRetorno.getCodigoPostal());
    }

    public List<DireccionDTO> listarDirecciones(Long id)
    {
        Cliente cliente = clienteValidations.validarExistencia(id);
        List<Direccion> direcciones = direccionRepository.findByClienteId(id);

        if(direcciones == null)
        {
            throw new RuntimeException("No hay direcciones aun en ese cliente");
        }

        return direcciones.stream().map(d -> new DireccionDTO(d.getId(), d.getDireccion(), d.getCiudad(), d.getPais(), d.getCodigoPostal())).collect(Collectors.toList());
    }
}
