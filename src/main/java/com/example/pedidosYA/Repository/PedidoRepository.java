package com.example.pedidosYA.Repository;

import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByRestauranteId(Long idRestaurante);
}
