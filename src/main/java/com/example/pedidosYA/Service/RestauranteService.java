package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.ClienteDTO.ResponseDTO;
import com.example.pedidosYA.DTO.DireccionDTO.DireccionDTO;
import com.example.pedidosYA.DTO.ProductoDTO.ProductoDetailDTO;
import com.example.pedidosYA.DTO.ProductoDTO.ProductoResumenDTO;
import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaResumenDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.*;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.*;
import com.example.pedidosYA.Repository.ProductoRepository;
import com.example.pedidosYA.Repository.RestauranteRepository;
import com.example.pedidosYA.Repository.UsuarioRepository;
import com.example.pedidosYA.Validations.RestauranteValidations;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private RestauranteValidations restauranteValidations;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProductoRepository productoRepository;

    public RestauranteDetailDTO findRestauranteByNombre(String usuario){

        Restaurante restaurante = restauranteRepository.findByUsuario(usuario).orElseThrow(()-> new RuntimeException("Restaurante no encontrado"));

        Set<ProductoResumenDTO> menuDTO = restaurante.getMenu().stream()
                .map(producto -> new ProductoResumenDTO(producto.getId(), producto.getNombre(), producto.getPrecio()))
                .collect(Collectors.toSet());

        List<ReseniaResumenDTO> reseniaDTO = restaurante.getReseniasRestaurante().stream()
                .map(resenia -> new ReseniaResumenDTO(resenia.getCliente().getId(), resenia.getDescripcion(), resenia.getPuntuacion()))
                .collect(Collectors.toList());

        List<DireccionDTO>direccionDTOS = restaurante.getDirecciones().stream().map(direccion ->
                new DireccionDTO(direccion.getId(), direccion.getDireccion(), direccion.getCiudad(), direccion.getPais(), direccion.getCodigoPostal())).collect(Collectors.toList());

        List<ComboResponseDTO> comboResponseDTOS = restaurante.getCombos().stream().map(combo -> new ComboResponseDTO(combo.getNombre(), combo.getProductos().stream().map(producto -> new ProductoResumenDTO(producto.getId(), producto.getNombre(), producto.getPrecio())).collect(Collectors.toSet())
                , combo.getDescuento(), combo.getPrecio())).collect(Collectors.toList());

        return new RestauranteDetailDTO(restaurante.getId(), restaurante.getNombre(), restaurante.getEmail(),
                menuDTO,comboResponseDTOS, reseniaDTO, direccionDTOS);
    }

    public Set<RestauranteResumenDTO> findAllRestaurantes(){
        List<Restaurante> lista = restauranteRepository.findAll();
        if (lista.isEmpty()) {
            throw new BusinessException("No hay restaurantes cargados actualmente");
        }
        return restauranteRepository.findAll().stream().map(r -> new RestauranteResumenDTO(r.getId(), r.getNombre())).collect(Collectors.toSet());
    }

    public void modificarContraseniaRestaurante (String usuario, RestauranteModificarDTO restauranteNuevo){
        Restaurante restaurante = restauranteRepository.findByUsuario(usuario).orElseThrow(()-> new RuntimeException("Restaurante no encontrado"));

        restauranteValidations.validarContraseniaActual(restaurante.getId(), restauranteNuevo.getContraseniaActual());

        restaurante.setContrasenia(passwordEncoder.encode(restauranteNuevo.getContraseniaNueva()));

        Restaurante r = restauranteRepository.save(restaurante);

    }

    public void modificarUsuarioNombreRestaurante (String usuario, RestauranteModificarDTO restauranteNuevo){
        Restaurante restaurante = restauranteRepository.findByUsuario(usuario).orElseThrow(()-> new RuntimeException("Restaurante no encontrado"));

        restauranteValidations.validarContraseniaActual(restaurante.getId(), restauranteNuevo.getContraseniaActual());
        restauranteValidations.validarNombreNoDuplicadoConID(restaurante.getId(), restauranteNuevo.getNombreRestaurante());

        restaurante.setUsuario(restauranteNuevo.getUsuario());
        restaurante.setNombre(restauranteNuevo.getNombreRestaurante());

        Restaurante r = restauranteRepository.save(restaurante);

    }

    public void modificarUsuarioNombreRestauranteAdmin (String usuario, RestauranteModificarDTO restauranteNuevo){
        Restaurante restaurante = restauranteRepository.findByUsuario(usuario).orElseThrow(()-> new RuntimeException("Restaurante no encontrado"));

        restauranteValidations.validarNombreNoDuplicadoConID(restaurante.getId(), restauranteNuevo.getNombreRestaurante());

        restaurante.setUsuario(restauranteNuevo.getUsuario());
        restaurante.setNombre(restauranteNuevo.getNombreRestaurante());

        Restaurante r = restauranteRepository.save(restaurante);

    }

    public RestauranteResponseDTO eliminarRestaurante (Long id){
        Restaurante restaurante = restauranteValidations.validarExisteId(id);

        RestauranteResponseDTO restauranteDTO = new RestauranteResponseDTO(
                restaurante.getId(),
                restaurante.getUsuario(),
                restaurante.getNombre(),
                restaurante.getEmail()
        );

        restauranteRepository.delete(restaurante);
        return restauranteDTO;
    }

    public EstadisticasDTO obtenerEstadisticas(String usuario)
    {
        Restaurante restaurante = restauranteRepository.findByUsuario(usuario).orElseThrow(()-> new RuntimeException("Restaurante no encontrado"));

        List<Pedido>pedidos = restaurante.getPedidos();

        int cantidadPedidos = pedidos.size();
        double ingresos = pedidos.stream().mapToDouble(Pedido::getTotal).sum();
        double calificacionPromedio = restaurante.getReseniasRestaurante().stream().mapToDouble(Resenia::getPuntuacion).average().orElse(0);

        return new EstadisticasDTO(cantidadPedidos, ingresos, calificacionPromedio);
    }

    public ComboResponseDTO agregarCombo(String usuario, ComboRequestDTO comboRequestDTO)
    {
        Restaurante restaurante = restauranteRepository.findByUsuario(usuario).orElseThrow(()-> new RuntimeException("Restaurante no encontrado"));

        Combo combo = new Combo();

        combo.setNombre(comboRequestDTO.getNombre());
        combo.setDescuento(comboRequestDTO.getDescuento());
        combo.setRestaurante(restaurante);

        Set<Producto>productoSet = new HashSet<>();

        for(Long id : comboRequestDTO.getProductoIds())
        {
            Producto producto = productoRepository.findById(id).orElseThrow(()-> new RuntimeException("Producto con id: "+id+" no encontrado"));
            productoSet.add(producto);
        }
        combo.setProductos(productoSet);
        double precioProductos = productoSet.stream().mapToDouble(Producto::getPrecio).sum();
        combo.setPrecio(precioProductos * (1 - (comboRequestDTO.getDescuento() / 100)));

        restaurante.getCombos().add(combo);
        restauranteRepository.save(restaurante);

        Set<ProductoResumenDTO> productosResumen = productoSet.stream().map(producto -> new ProductoResumenDTO(producto.getId(),producto.getNombre(),producto.getPrecio())).collect(Collectors.toSet());

        return new ComboResponseDTO(combo.getNombre(), productosResumen, comboRequestDTO.getDescuento(), combo.getPrecio());
    }

    public List<ComboResponseDTO> verCombos(String usuario) {
        Restaurante restaurante = restauranteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        List<ComboResponseDTO> listaCombos = restaurante.getCombos().stream().map(combo -> {Set<ProductoResumenDTO> productosResumen = combo.getProductos().stream().map(producto -> new ProductoResumenDTO(producto.getId(), producto.getNombre(), producto.getPrecio())).collect(Collectors.toSet());
            return new ComboResponseDTO(combo.getNombre(), productosResumen, combo.getDescuento(), combo.getPrecio());}).collect(Collectors.toList());

        return listaCombos;
    }

    public BalanceDTO obtenerBalance(String usuario, String tipoFiltro, String fecha, String mes) {
        Restaurante restaurante = restauranteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        List<Pedido> pedidosValidos = restaurante.getPedidos().stream()
                .filter(pedido -> {
                    String estado = pedido.getEstado().toString();
                    return estado.equals("ENVIADO");
                })
                .collect(Collectors.toList());

        List<Pedido> pedidosFiltrados;

        if ("dia".equals(tipoFiltro) && fecha != null && !fecha.isEmpty()) {
            LocalDate fechaBuscada = LocalDate.parse(fecha);

            pedidosFiltrados = pedidosValidos.stream()
                    .filter(pedido -> {
                        LocalDateTime fechaPedido = pedido.getFechaPedido();
                        if (fechaPedido != null) {
                            return fechaPedido.toLocalDate().equals(fechaBuscada);
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        } else if ("mes".equals(tipoFiltro) && mes != null && !mes.isEmpty()) {
            String[] partes = mes.split("-");
            int anio = Integer.parseInt(partes[0]);
            int mesNum = Integer.parseInt(partes[1]);

            pedidosFiltrados = pedidosValidos.stream()
                    .filter(pedido -> {
                        LocalDateTime fechaPedido = pedido.getFechaPedido();
                        if (fechaPedido != null) {
                            return fechaPedido.getYear() == anio &&
                                    fechaPedido.getMonthValue() == mesNum;
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        } else {
            pedidosFiltrados = pedidosValidos;
        }

        System.out.println("Pedidos filtrados: " + pedidosFiltrados.size());

        int cantidadPedidos = pedidosFiltrados.size();
        double totalRecaudado = pedidosFiltrados.stream()
                .mapToDouble(Pedido::getTotal)
                .sum();
        double promedioVenta = cantidadPedidos > 0 ? totalRecaudado / cantidadPedidos : 0.0;

        return new BalanceDTO(totalRecaudado, cantidadPedidos, promedioVenta);
    @Transactional
    public void actualizarPerfilRestaurante(String usuario, ActualizarPerfilRestauranteDTO perfilDTO) {
        Restaurante restaurante = restauranteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        // Verificar contraseña actual
        restauranteValidations.validarContraseniaActual(restaurante.getId(), perfilDTO.getContraseniaActual());

        // Actualizar campos si se proporcionan
        if (perfilDTO.getNombreRestaurante() != null && !perfilDTO.getNombreRestaurante().trim().isEmpty()) {
            restauranteValidations.validarNombreNoDuplicadoConID(restaurante.getId(), perfilDTO.getNombreRestaurante());
            restaurante.setNombre(perfilDTO.getNombreRestaurante());
        }

        if (perfilDTO.getEmail() != null && !perfilDTO.getEmail().trim().isEmpty()) {
            restauranteValidations.validarEmailModificacion(restaurante.getId(), perfilDTO.getEmail());
            restaurante.setEmail(perfilDTO.getEmail());
        }

        restauranteRepository.save(restaurante);
    }

    @Transactional
    public void cambiarContraseniaRestaurante(String usuario, CambiarContraseniaRestauranteDTO contraseniaDTO) {
        Restaurante restaurante = restauranteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        // Validar contraseña actual
        restauranteValidations.validarContraseniaActual(restaurante.getId(), contraseniaDTO.getContraseniaActual());

        // Validar que las nuevas contraseñas coincidan
        if (!contraseniaDTO.getContraseniaNueva().equals(contraseniaDTO.getConfirmarContrasenia())) {
            throw new RuntimeException("Las contraseñas nuevas no coinciden");
        }

        // Validar nueva contraseña
        restauranteValidations.validarContrasenia(contraseniaDTO.getContraseniaNueva());

        // Actualizar contraseña
        restaurante.setContrasenia(passwordEncoder.encode(contraseniaDTO.getContraseniaNueva()));
        restauranteRepository.save(restaurante);
    }
}
