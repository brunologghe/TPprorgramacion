package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.ClienteDTO.ClienteCrearDTO;
import com.example.pedidosYA.DTO.ClienteDTO.ResponseDTO;
import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Usuario;
import com.example.pedidosYA.Repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public ResponseDTO crearUsuario(ClienteCrearDTO r) {
        Cliente c = new Cliente();
        c.setNombreYapellido(r.getNombreYapellido());
        c.setUsuario(r.getUsuario());
        c.setContrasenia(r.getContrasenia());

        Cliente cliente = clienteRepository.save(c);

        return new ResponseDTO(c.getId(), c.getUsuario(), c.getNombreYapellido());
    }

}
