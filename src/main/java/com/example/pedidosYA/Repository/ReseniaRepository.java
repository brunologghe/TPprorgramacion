package com.example.pedidosYA.Repository;

import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Pedido;
import com.example.pedidosYA.Model.Resenia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReseniaRepository extends JpaRepository<Resenia, Long> {
    List<Resenia> findByRestauranteId(Long idRestaurante);
}
