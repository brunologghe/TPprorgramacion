package com.example.pedidosYA.Repository;

import com.example.pedidosYA.Model.RolUsuario;
import com.example.pedidosYA.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsuario(String usuario);
    boolean existsByUsuario(String usuario);
    boolean existsByRol(RolUsuario rol);
}
