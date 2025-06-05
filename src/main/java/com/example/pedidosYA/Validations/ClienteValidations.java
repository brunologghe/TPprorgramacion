package com.example.pedidosYA.Validations;

import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Repository.ClienteRepository;
import com.example.pedidosYA.Repository.DireccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.pedidosYA.Exceptions.BusinessException;

@Component
public class ClienteValidations {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private DireccionRepository direccionRepository;

    public Cliente validarExistencia(Long id)
    {
        return clienteRepository.findById(id).orElseThrow(()-> new RuntimeException("No existe ningun cliente con ese id"));
    }

    public void validarDireccion(Long idDireccion, Long idCliente)
    {
        if(!direccionRepository.existsByIdAndClienteId(idCliente, idDireccion)){
            throw new BusinessException("No existe esa direccion ese cliente");
        }
    }
}
