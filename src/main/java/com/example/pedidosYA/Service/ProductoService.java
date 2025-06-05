package com.example.pedidosYA.Service;

<<<<<<< Updated upstream
=======
import com.example.pedidosYA.DTO.ProductoDTO.ProductoResumenDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteResumenDTO;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.Producto;
import com.example.pedidosYA.Model.Restaurante;
import com.example.pedidosYA.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
>>>>>>> Stashed changes
public class ProductoService {

    @Autowired
    ProductoRepository productoRepository;

    public Set<ProductoResumenDTO> findAllProductosByRestauranteId(Long id){


        List<Producto> lista = productoRepository.findAllByRestauranteId(id);
        if (lista.isEmpty()) {
            throw new BusinessException("No hay productos cargados actualmente");
        }
        return productoRepository.findAll().stream().map(p -> new ProductoResumenDTO(p.getId(), p.getNombre(), p.getPrecio())).collect(Collectors.toSet());
    }
}
