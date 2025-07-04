package com.example.pedidosYA.Repository;

import com.example.pedidosYA.Model.MetodoDePago;
import com.example.pedidosYA.Model.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {
}
