package com.example.pedidosYA.Repository;

import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Pedido;
import com.example.pedidosYA.Model.Resenia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ReseniaRepository extends JpaRepository<Resenia, Long> {
    List<Resenia> findByRestauranteId(Long idRestaurante);
}
