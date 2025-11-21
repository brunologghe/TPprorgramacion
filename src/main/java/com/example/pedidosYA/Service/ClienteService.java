package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.ClienteDTO.ActualizarPerfilDTO;
import com.example.pedidosYA.DTO.ClienteDTO.CambiarContraseniaDTO;
import com.example.pedidosYA.DTO.ClienteDTO.ClienteCrearDTO;
import com.example.pedidosYA.DTO.ClienteDTO.ClienteDetailDto;
import com.example.pedidosYA.DTO.ClienteDTO.ModificarDTO;
import com.example.pedidosYA.DTO.ClienteDTO.ResponseDTO;
import com.example.pedidosYA.DTO.ProductoDTO.ProductoResumenDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.*;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Restaurante;
import com.example.pedidosYA.Model.RolUsuario;
import com.example.pedidosYA.Model.Usuario;
import com.example.pedidosYA.Repository.ClienteRepository;
import com.example.pedidosYA.Repository.RestauranteRepository;
import com.example.pedidosYA.Validations.ClienteValidations;
import jakarta.transaction.Transactional;
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
                .map(cliente -> new ResponseDTO(cliente.getId(), cliente.getUsuario(), cliente.getNombreYapellido(), cliente.getEmail()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseDTO eliminar(Long id){
        Cliente cliente = clienteValidations.validarExistencia(id);
        ResponseDTO clienteDTO = new ResponseDTO(
                cliente.getId(),
                cliente.getUsuario(),
                cliente.getNombreYapellido(),
                cliente.getEmail()
        );

        clienteRepository.deleteById(id);
        return clienteDTO;
    }

    @Transactional
    public void modificarContrasenia (String usuario, ModificarDTO clienteNuevo){
        Cliente cliente = clienteRepository.findByUsuario(usuario).orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        clienteValidations.validarContraseniaActual(cliente.getId(), clienteNuevo.getContraseniaActual());

        cliente.setContrasenia(passwordEncoder.encode(clienteNuevo.getContraseniaNueva()));

        Cliente c = clienteRepository.save(cliente);
    }

    @Transactional
    public void modificarUsuarioNombre (String usuario, ModificarDTO clienteNuevo){
        Cliente cliente = clienteRepository.findByUsuario(usuario).orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        clienteValidations.validarContraseniaActual(cliente.getId(), clienteNuevo.getContraseniaActual());
        clienteValidations.validarNombreNoDuplicadoConID(cliente.getId(), clienteNuevo.getNombreYapellido());

        cliente.setNombreYapellido(clienteNuevo.getNombreYapellido());
        cliente.setUsuario(clienteNuevo.getUsuario());
        if (clienteNuevo.getEmail() != null) {
            clienteValidations.validarEmail(clienteNuevo.getEmail());
            cliente.setEmail(clienteNuevo.getEmail());
        }

        Cliente c = clienteRepository.save(cliente);
    }

    @Transactional
    public void modificarUsuarioNombreAdmin (String usuario, ModificarDTO clienteNuevo){
        Cliente cliente = clienteRepository.findByUsuario(usuario).orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        clienteValidations.validarNombreNoDuplicadoConID(cliente.getId(), clienteNuevo.getNombreYapellido());

        cliente.setNombreYapellido(clienteNuevo.getNombreYapellido());
        cliente.setUsuario(clienteNuevo.getUsuario());
        if (clienteNuevo.getEmail() != null) {
            clienteValidations.validarEmail(clienteNuevo.getEmail());
            cliente.setEmail(clienteNuevo.getEmail());
        }

        Cliente c = clienteRepository.save(cliente);
    }



    public ClienteDetailDto verUsuarioPorNombre(String nombreUsuario) {

        Cliente cliente = clienteRepository.findByUsuario(nombreUsuario).orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        return new ClienteDetailDto(cliente.getId(), cliente.getUsuario(), cliente.getNombreYapellido(), cliente.getEmail(), cliente.getDirecciones(), cliente.getTarjetas());
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

    @Transactional
    public void actualizarPerfil(String usuario, ActualizarPerfilDTO perfilDTO) {
        Cliente cliente = clienteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        // Verificar contraseña actual
        clienteValidations.validarContraseniaActual(cliente.getId(), perfilDTO.getContraseniaActual());

        // Actualizar campos si se proporcionan
        if (perfilDTO.getNombreYapellido() != null && !perfilDTO.getNombreYapellido().trim().isEmpty()) {
            clienteValidations.validarNombreNoDuplicadoConID(cliente.getId(), perfilDTO.getNombreYapellido());
            cliente.setNombreYapellido(perfilDTO.getNombreYapellido());
        }

        if (perfilDTO.getEmail() != null && !perfilDTO.getEmail().trim().isEmpty()) {
            clienteValidations.validarEmailModificacion(cliente.getId(), perfilDTO.getEmail());
            cliente.setEmail(perfilDTO.getEmail());
        }

        clienteRepository.save(cliente);
    }

    @Transactional
    public void cambiarContrasenia(String usuario, CambiarContraseniaDTO contraseniaDTO) {
        Cliente cliente = clienteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        // Validar contraseña actual
        clienteValidations.validarContraseniaActual(cliente.getId(), contraseniaDTO.getContraseniaActual());

        // Validar que las nuevas contraseñas coincidan
        if (!contraseniaDTO.getContraseniaNueva().equals(contraseniaDTO.getConfirmarContrasenia())) {
            throw new BusinessException("Las contraseñas nuevas no coinciden");
        }

        // Validar nueva contraseña
        clienteValidations.validarContrasenia(contraseniaDTO.getContraseniaNueva());

        // Actualizar contraseña
        cliente.setContrasenia(passwordEncoder.encode(contraseniaDTO.getContraseniaNueva()));
        clienteRepository.save(cliente);

    public MenuComboDTO verMenuRestaurante(Long id)
    {
        Restaurante restaurante = restauranteRepository.findById(id).orElseThrow(()-> new RuntimeException("Restaurante no encontrado"));

        return new MenuComboDTO(restaurante.getMenu().stream().map(producto -> new ProductoResumenDTO(producto.getId(), producto.getNombre(), producto.getPrecio())).collect(Collectors.toList()),
        restaurante.getCombos().stream().map(combo -> new ComboResponseDTO(combo.getNombre(), combo.getProductos().stream().map(producto -> new ProductoResumenDTO(producto.getId(), producto.getNombre(), producto.getPrecio())).collect(Collectors.toSet()), combo.getDescuento(), combo.getPrecio())).collect(Collectors.toList()));

    }
}
