package com.example.pedidosYA.Validations;

import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Direccion;
import com.example.pedidosYA.Repository.DireccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DireccionValidations {

    @Autowired
    DireccionRepository direccionRepository;

    public void validarDireccionDuplicadaPorCliente(Cliente cliente, Direccion direccion) {
        Direccion direccionExistente = direccionRepository
                .findByClienteIdAndDireccionAndCodigoPostal(cliente.getId(), direccion.getDireccion(), direccion.getCodigoPostal());

        if (direccionExistente != null && !direccionExistente.getId().equals(direccion.getId())) {
            throw new BusinessException("Ya existe una dirección igual para este cliente.");
        }
    }

    public void validarDirecciones(List<Direccion> direcciones){
        if (direcciones == null || direcciones.isEmpty()) {
            throw new RuntimeException("No hay direcciones aún para este cliente");
        }
    }


}
