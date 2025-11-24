package com.example.pedidosYA.Repository;

import com.example.pedidosYA.Model.EstadoPedido;
import com.example.pedidosYA.Model.Pedido;
import com.example.pedidosYA.Model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByRestauranteId(Long idRestaurante);

    List<Pedido> findByRestauranteAndEstadoAndFechaPedidoBetween(
            Restaurante restaurante,
            EstadoPedido estado,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin
    );

    List<Pedido> findByRestauranteAndEstado(Restaurante restaurante, EstadoPedido estado); // NUEVO

    List<Pedido> findByRestauranteAndEstadoIn(Restaurante restaurante, Collection<EstadoPedido> estados);
}

