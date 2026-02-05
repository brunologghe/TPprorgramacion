package com.example.pedidosYA.Repository;

import com.example.pedidosYA.Model.Repartidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepartidorRepository extends JpaRepository<Repartidor, Long> {
    Optional<Repartidor> findByUsuario(String nombreUsuario);
    Optional<Repartidor> findByEmail(String email);
    boolean existsByUsuario(String usuario);
    boolean existsByEmail(String email);
}
