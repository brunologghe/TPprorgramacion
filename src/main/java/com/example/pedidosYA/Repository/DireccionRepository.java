package com.example.pedidosYA.Repository;

import com.example.pedidosYA.DTO.DireccionDTO.DireccionDTO;
import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    Direccion findByClienteIdAndCodigoPostalAndDireccion(Long clienteId, String codigoPostal, String direccion);
    List<Direccion> findByClienteId(Long id);
    Boolean existsByIdAndClienteId(Long idCliente, Long idDireccion);
}
