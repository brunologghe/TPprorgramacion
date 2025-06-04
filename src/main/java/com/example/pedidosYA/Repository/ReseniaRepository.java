package com.example.pedidosYA.Repository;

import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Resenia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReseniaRepository extends JpaRepository<Resenia, Long> {
}
