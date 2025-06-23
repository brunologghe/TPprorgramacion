package com.example.pedidosYA.Repository;

import com.example.pedidosYA.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByUsuario (String nombreUsuario);
    Cliente findByNombreYapellido(String nombreYapellido);
    boolean existsByNombreYapellido (String nombreYapellido);
}
