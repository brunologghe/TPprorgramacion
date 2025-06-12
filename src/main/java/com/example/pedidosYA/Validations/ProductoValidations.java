package com.example.pedidosYA.Validations;

import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.Producto;
import com.example.pedidosYA.Model.Restaurante;
import com.example.pedidosYA.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductoValidations {

    @Autowired
    ProductoRepository productoRepository;

    public void validarNombreProductoExistente (String nombre){
        if(!productoRepository.existsByNombre(nombre)){
            throw new BusinessException("El nombre de este producto no existe");
        }
    }

    public void validarNombreProductoNoDuplicado(Long id, String nombre)throws BusinessException{
        Producto p = productoRepository.findByNombre(nombre);
        if (p != null && p.getId() != id) {
            throw new BusinessException("El nombre ya pertenece a otro restaurante.");
        }
    }

    public void validarProductoPerteneceARestaurante(Long idRestaurante, Producto producto){
        if (!producto.getRestaurante().getId().equals(idRestaurante)) {
            throw new BusinessException("El producto no pertenece al restaurante elegido");
        }
    }

    public void validarProductoEnRestaurante(Restaurante restaurante, Producto producto){
        if(!restaurante.getMenu().contains(producto)){
            throw new BusinessException("El restaurante no contiene este producto");
        }
    }
}
