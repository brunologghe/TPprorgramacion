package com.example.pedidosYA.Validations;

import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Restaurante;
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
        return clienteRepository.findById(id).orElseThrow(()-> new BusinessException("No existe ningun cliente con ese id"));
    }

    public void validarDireccion(Long idDireccion, Long idCliente)
    {
        if(!direccionRepository.existsByIdAndClienteId(idDireccion, idCliente)){
            throw new BusinessException("No existe esa direccion ese cliente");
        }
    }

    public void validarContraseniaActual(Long id, String contrasenia){

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado con id: " + id));

        if (!cliente.getContrasenia().equals(contrasenia)) {
            throw new BusinessException("La contraseña actual es incorrecta.");
        }
    }
}
