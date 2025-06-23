package com.example.pedidosYA.Repository;

import com.example.pedidosYA.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByUsuario (String nombreUsuario);
    Cliente findByNombreYapellido(String nombreYapellido);
    boolean existsByNombreYapellido (String nombreYapellido);
}
