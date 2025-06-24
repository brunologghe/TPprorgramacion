package com.example.pedidosYA.Validations;

import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaResumenDTO;
import com.example.pedidosYA.Exceptions.BusinessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReseniaValidations {

    public void validarResenia (List<ReseniaResumenDTO> resenias){
        if(resenias.isEmpty()){
            throw new BusinessException("No hay resenias en este restaurante");
        }
    }
}
