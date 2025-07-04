package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.PagoDTO.TarjetaMuestraDTO;
import com.example.pedidosYA.DTO.PagoDTO.TarjetaRequestDTO;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Tarjeta;
import com.example.pedidosYA.Repository.ClienteRepository;
import com.example.pedidosYA.Repository.TarjetaRepository;
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
    private TarjetaRepository tarjetaRepository;

    public TarjetaMuestraDTO agregarTarjeta(String username, TarjetaRequestDTO tarjetaRequestDTO) {
        Cliente cliente = clienteRepository.findByUsuario(username).orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        clienteValidations.validarExistencia(cliente.getId());

        Tarjeta tarjeta = new Tarjeta(tarjetaRequestDTO.getTipo(), tarjetaRequestDTO.getNumero(), tarjetaRequestDTO.getTitular(), tarjetaRequestDTO.getVencimiento(), tarjetaRequestDTO.getCvv(), cliente);

        clienteValidations.validarExistenciaTarjeta(tarjeta, cliente);

        Tarjeta tarjetaRetorno = tarjetaRepository.save(tarjeta);
        cliente.getTarjetas().add(tarjetaRetorno);

        return new TarjetaMuestraDTO(tarjetaRetorno.getId(), tarjetaRetorno.getTipo(), tarjetaRetorno.getNumeroEnmascarado(), tarjetaRetorno.getVencimiento());
    }

    public void eliminarTarjeta(String username, Long idPago) {
        Cliente cliente = clienteRepository.findByUsuario(username).orElseThrow(() -> new BusinessException("Cliente no encontrado"));
        Tarjeta pago = tarjetaRepository.findById(idPago)
                .orElseThrow(() -> new BusinessException("No existe ese método de pago"));

        clienteValidations.validarPagoEnCliente(pago, cliente);

        cliente.getTarjetas().remove(pago);
        tarjetaRepository.delete(pago);
    }

    public List<Tarjeta> mostarTarjetas(String username) {
        Cliente cliente = clienteRepository.findByUsuario(username).orElseThrow(() -> new BusinessException("Cliente no encontrado"));
        return cliente.getTarjetas();
    }
}
