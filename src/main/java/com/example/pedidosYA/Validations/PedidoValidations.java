package com.example.pedidosYA.Validations;

import com.example.pedidosYA.DTO.PedidoDTO.DetallePedidoDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoDetailDTO;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public class PedidoValidations {

    @Autowired
    PedidoService pedidoService;

    public void verificarDetallesPedido (List<DetallePedidoDTO> detallePedido){

        if(detallePedido.isEmpty()){
            throw new BusinessException("No hay detalles de pedido en la lista");
        }
    }

    public void verificarPedidoDetailDTO (List<PedidoDetailDTO> detallePedido){

        if(detallePedido.isEmpty()){
            throw new BusinessException("No hay detalles de pedido en la lista");
        }
    }
}
