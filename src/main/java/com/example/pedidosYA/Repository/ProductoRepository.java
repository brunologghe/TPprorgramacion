package com.example.pedidosYA.Repository;

import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findAllByRestauranteId(Long id);
    boolean existsByNombre (String nombre);
    Producto findByNombre (String nombre);
    Producto findByNombreAndRestauranteId(String nombre, Long idRestaurante);
}
