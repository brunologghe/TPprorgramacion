package com.example.pedidosYA.Repository;

import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findAllByRestauranteId(Long id);
    boolean existsByNombre (String nombre);
    Producto findByNombre (String nombre);
    Producto findByNombreAndRestauranteId(String nombre, Long idRestaurante);
}
