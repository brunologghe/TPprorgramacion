package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.ClienteDTO.ClienteCrearDTO;
import com.example.pedidosYA.DTO.ClienteDTO.ResponseDTO;
import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Usuario;
import com.example.pedidosYA.Repository.ClienteRepository;
import com.example.pedidosYA.Validations.ClienteValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ClienteValidations clienteValidations;

    public ResponseDTO crearUsuario(ClienteCrearDTO r) {
        Cliente c = new Cliente();
        c.setNombreYapellido(r.getNombreYapellido());
        c.setUsuario(r.getUsuario());
        c.setContrasenia(r.getContrasenia());

        Cliente cliente = clienteRepository.save(c);

        return new ResponseDTO(c.getId(), c.getUsuario(), c.getNombreYapellido());
    }

    public List<ResponseDTO> listAll(){

        return clienteRepository.findAll().stream()
                .map(cliente -> new ResponseDTO(cliente.getId(), cliente.getUsuario(), cliente.getNombreYapellido()))
                .collect(Collectors.toList());
    }

    public ResponseDTO eliminar(Long id){
        Cliente cliente = clienteValidations.validarExistencia(id);
        ResponseDTO clienteDTO = new ResponseDTO(
                cliente.getId(),
                cliente.getUsuario(),
                cliente.getNombreYapellido()
        );

        clienteRepository.deleteById(id);
        return clienteDTO;
    }
}
