package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.ClienteDTO.ClienteCrearDTO;
import com.example.pedidosYA.DTO.ClienteDTO.ClienteDetailDto;
import com.example.pedidosYA.DTO.ClienteDTO.ModificarDTO;
import com.example.pedidosYA.DTO.ClienteDTO.ResponseDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteModificarDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteResponseDTO;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Restaurante;
import com.example.pedidosYA.Model.RolUsuario;
import com.example.pedidosYA.Model.Usuario;
import com.example.pedidosYA.Repository.ClienteRepository;
import com.example.pedidosYA.Validations.ClienteValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ClienteValidations clienteValidations;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<ResponseDTO> listAll(){

        return clienteRepository.findAll().stream()
                .map(cliente -> new ResponseDTO(cliente.getId(), cliente.getUsuario(), cliente.getNombreYapellido()))
                .collect(Collectors.toList());
    }

    public ResponseDTO eliminar(Long id){
        Cliente cliente = clienteValidations.validarExistencia(id);
        ResponseDTO clienteDTO = new ResponseDTO(
                cliente.getId(),
                cliente.getUsuario(),
                cliente.getNombreYapellido()
        );

        clienteRepository.deleteById(id);
        return clienteDTO;
    }

    public ResponseDTO modificar (Long id, ModificarDTO clienteNuevo){
        clienteValidations.validarContraseniaActual(id, clienteNuevo.getContraseniaActual());

        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        cliente.setNombreYapellido(clienteNuevo.getNombreYapellido());
        cliente.setUsuario(clienteNuevo.getUsuario());
        cliente.setContrasenia(passwordEncoder.encode(clienteNuevo.getContraseniaNueva()));

        clienteRepository.save(cliente);

        return new ResponseDTO(cliente.getId(), cliente.getUsuario(), cliente.getNombreYapellido());

    }

    public ClienteDetailDto verUsuarioPorNombre(String nombreUsuario) {
        Cliente cliente = clienteRepository.findByUsuario(nombreUsuario);

        return new ClienteDetailDto(cliente.getId(), cliente.getUsuario(), cliente.getNombreYapellido(), cliente.getDirecciones(), cliente.getMetodosPago());
    }

    public Cliente findByUsuario(String usuario) {
        return clienteRepository.findByUsuario(usuario);
    }
}
