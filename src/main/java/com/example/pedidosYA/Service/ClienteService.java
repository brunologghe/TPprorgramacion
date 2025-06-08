package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.ClienteDTO.ClienteCrearDTO;
import com.example.pedidosYA.DTO.ClienteDTO.ClienteDetailDto;
import com.example.pedidosYA.DTO.ClienteDTO.ResponseDTO;
import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Usuario;
import com.example.pedidosYA.Repository.ClienteRepository;
import com.example.pedidosYA.Validations.ClienteValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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


    public ClienteDetailDto verUsuario(Long id)
    {
        Cliente cliente = clienteValidations.validarExistencia(id);

        return new ClienteDetailDto(cliente.getId(), cliente.getUsuario(), cliente.getNombreYapellido(), cliente.getDirecciones(), cliente.getMetodosPago());
    }
}
