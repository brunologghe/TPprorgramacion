package com.example.pedidosYA.Repository;

import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByRestauranteId(Long idRestaurante);
}
