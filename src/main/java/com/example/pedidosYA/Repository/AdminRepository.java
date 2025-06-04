package com.example.pedidosYA.Repository;

import com.example.pedidosYA.Model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,Long> {
    Optional<Admin> findByUsuario(String nombre);
}
