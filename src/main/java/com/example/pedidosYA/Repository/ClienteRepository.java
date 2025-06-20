package com.example.pedidosYA.Repository;

import com.example.pedidosYA.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByUsuario (String nombreUsuario);
}
