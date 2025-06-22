package com.example.pedidosYA.Repository;

import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    Direccion findByClienteIdAndCodigoPostalAndDireccion(Long clienteId, String codigoPostal, String direccion);
    List<Direccion> findByClienteId(Long id);
    Boolean existsByIdAndClienteId(Long idCliente, Long idDireccion);
}
