package com.example.pedidosYA.Repository;

import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.EstadoRestaurante;
import com.example.pedidosYA.Model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    boolean existsByNombre(String nombre);
    Restaurante findByNombre(String nombre);
    Optional<Restaurante> findByUsuario (String nombreUsuario);

    List<Restaurante> findByEstado(EstadoRestaurante estado);

    @Query("SELECT r FROM Restaurante r WHERE r.estado = 'APROBADO'")
    List<Restaurante> findAprobados();

    long countByEstado(EstadoRestaurante estado);
}
