package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.PagoDTO.PagoMuestraDTO;
import com.example.pedidosYA.DTO.PagoDTO.PagoRequestDTO;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.MetodoDePago;
import com.example.pedidosYA.Model.Pago;
import com.example.pedidosYA.Repository.ClienteRepository;
import com.example.pedidosYA.Repository.PagoRepository;
import com.example.pedidosYA.Validations.ClienteValidations;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagoService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ClienteValidations clienteValidations;
    @Autowired
    private PagoRepository pagoRepository;

    @Transactional
    public PagoMuestraDTO agregarPago(String username, PagoRequestDTO metodoDePago) {
        Cliente cliente = clienteRepository.findByUsuario(username).orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        clienteValidations.validarExistencia(cliente.getId());

        Pago pago = new Pago(metodoDePago.getMetodoDePago(), cliente);
        Pago pagoRetorno = pagoRepository.save(pago);
        cliente.getMetodosPago().add(pagoRetorno);

        return new PagoMuestraDTO(pago.getId(), pago.getMetodoDePago());
    }

    @Transactional
    public void eliminarPago(String username, Long idPago) {
        Cliente cliente = clienteRepository.findByUsuario(username).orElseThrow(() -> new BusinessException("Cliente no encontrado"));
        Pago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new BusinessException("No existe ese método de pago"));

        clienteValidations.validarPagoEnCliente(pago, cliente);

        cliente.getMetodosPago().remove(pago);
        pagoRepository.delete(pago);
    }

    public List<Pago> mostarPagos(String username) {
        Cliente cliente = clienteRepository.findByUsuario(username).orElseThrow(() -> new BusinessException("Cliente no encontrado"));
        return cliente.getMetodosPago();
    }
}
