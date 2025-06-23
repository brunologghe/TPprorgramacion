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

    public void verificarCantidadPedido (int cantidad){
        if(cantidad<1 || cantidad>15){
            throw new BusinessException("La cantidad debe ser de entre 1 y 15 elementos del mismo producto");
        }
    }
}
