package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.PagoDTO.PagoMuestraDTO;
import com.example.pedidosYA.DTO.PagoDTO.PagoRequestDTO;
import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.MetodoDePago;
import com.example.pedidosYA.Model.Pago;
import com.example.pedidosYA.Repository.ClienteRepository;
import com.example.pedidosYA.Repository.PagoRepository;
import com.example.pedidosYA.Validations.ClienteValidations;
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

    public PagoMuestraDTO agregarPago(Long id, PagoRequestDTO metodoDePago)
    {
        Cliente cliente = clienteValidations.validarExistencia(id);

        Pago pago = new Pago(metodoDePago.getMetodoDePago(), cliente);
        Pago pagoRetorno = pagoRepository.save(pago);
        cliente.getMetodosPago().add(pagoRetorno);

        return new PagoMuestraDTO(pago.getId(), pago.getMetodoDePago(), pago.getCliente().getId());
    }

    public void eliminarPago(Long idCliente, Long idPago) {
        Cliente cliente = clienteValidations.validarExistencia(idCliente);

        Pago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new RuntimeException("No existe un método de pago con ese id"));

        if (!pago.getCliente().getId().equals(cliente.getId())) {
            throw new RuntimeException("Este método de pago no pertenece al cliente indicado");
        }

        cliente.getMetodosPago().remove(pago);
        pagoRepository.delete(pago);
    }

    public List<Pago> mostarPagos(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("No existe un cliente con ese id"));
        return pagoRepository.findByClienteId(id);
    }
}
