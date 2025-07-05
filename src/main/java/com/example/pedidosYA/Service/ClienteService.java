package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.ClienteDTO.ClienteCrearDTO;
import com.example.pedidosYA.DTO.ClienteDTO.ClienteDetailDto;
import com.example.pedidosYA.DTO.ClienteDTO.ModificarDTO;
import com.example.pedidosYA.DTO.ClienteDTO.ResponseDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteModificarDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteResponseDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteResumenDTO;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Restaurante;
import com.example.pedidosYA.Model.RolUsuario;
import com.example.pedidosYA.Model.Usuario;
import com.example.pedidosYA.Repository.ClienteRepository;
import com.example.pedidosYA.Repository.RestauranteRepository;
import com.example.pedidosYA.Validations.ClienteValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private RestauranteRepository restauranteRepository;

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

    public void modificarContrasenia (String usuario, ModificarDTO clienteNuevo){
        Cliente cliente = clienteRepository.findByUsuario(usuario).orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        clienteValidations.validarContraseniaActual(cliente.getId(), clienteNuevo.getContraseniaActual());

        cliente.setContrasenia(passwordEncoder.encode(clienteNuevo.getContraseniaNueva()));

        Cliente c = clienteRepository.save(cliente);
    }

    public void modificarUsuarioNombre (String usuario, ModificarDTO clienteNuevo){
        Cliente cliente = clienteRepository.findByUsuario(usuario).orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        clienteValidations.validarContraseniaActual(cliente.getId(), clienteNuevo.getContraseniaActual());
        clienteValidations.validarNombreNoDuplicadoConID(cliente.getId(), clienteNuevo.getNombreYapellido());

        cliente.setNombreYapellido(clienteNuevo.getNombreYapellido());
        cliente.setUsuario(clienteNuevo.getUsuario());

        Cliente c = clienteRepository.save(cliente);
    }

    public void modificarUsuarioNombreAdmin (String usuario, ModificarDTO clienteNuevo){
        Cliente cliente = clienteRepository.findByUsuario(usuario).orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        clienteValidations.validarNombreNoDuplicadoConID(cliente.getId(), clienteNuevo.getNombreYapellido());

        cliente.setNombreYapellido(clienteNuevo.getNombreYapellido());
        cliente.setUsuario(clienteNuevo.getUsuario());

        Cliente c = clienteRepository.save(cliente);
    }



    public ClienteDetailDto verUsuarioPorNombre(String nombreUsuario) {

        Cliente cliente = clienteRepository.findByUsuario(nombreUsuario).orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        return new ClienteDetailDto(cliente.getId(), cliente.getUsuario(), cliente.getNombreYapellido(), cliente.getDirecciones(), cliente.getTarjetas());
    }

    public RestauranteResumenDTO agregarRestauranteALista(String usuario, Long id)
    {
        Cliente cliente = clienteRepository.findByUsuario(usuario).orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        Restaurante restaurante = restauranteRepository.findById(id).orElseThrow(() -> new BusinessException("Restaurante no encontrado"));

        cliente.getListaRestaurantesFavoritos().add(restaurante);

        clienteRepository.save(cliente);

        return new RestauranteResumenDTO(restaurante.getId(), restaurante.getNombre());
    }

    public List<RestauranteResumenDTO> verListaFavoritos(String usuario)
    {
        Cliente cliente = clienteRepository.findByUsuario(usuario).orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        List<RestauranteResumenDTO>listaFav = new ArrayList<>();

        for(Restaurante r : cliente.getListaRestaurantesFavoritos()){
            RestauranteResumenDTO restauranteResumenDTO = new RestauranteResumenDTO(r.getId(), r.getNombre());
            listaFav.add(restauranteResumenDTO);
        }

        if(listaFav.isEmpty())
        {
            throw new RuntimeException("Aun no hay restaurantes en la lista de favoritos");
        }

        return listaFav;
    }
}
