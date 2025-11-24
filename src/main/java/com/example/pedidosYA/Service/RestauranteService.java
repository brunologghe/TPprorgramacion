package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.ClienteDTO.ResponseDTO;
import com.example.pedidosYA.DTO.DireccionDTO.DireccionDTO;
import com.example.pedidosYA.DTO.ProductoDTO.ProductoDetailDTO;
import com.example.pedidosYA.DTO.ProductoDTO.ProductoResumenDTO;
import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaResumenDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.*;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.*;
import com.example.pedidosYA.Repository.PedidoRepository;
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
    @Autowired
    private PedidoRepository pedidoRepository;

    public RestauranteDetailDTO findRestauranteByNombre(String usuario){

        Restaurante restaurante = restauranteRepository.findByUsuario(usuario).orElseThrow(()-> new RuntimeException("Restaurante no encontrado"));

        Set<ProductoResumenDTO> menuDTO = restaurante.getMenu().stream()
                .map(producto -> new ProductoResumenDTO(producto.getId(), producto.getNombre(), producto.getPrecio(), producto.getStock()))
                .collect(Collectors.toSet());

        List<ReseniaResumenDTO> reseniaDTO = restaurante.getReseniasRestaurante().stream()
                .map(resenia -> new ReseniaResumenDTO(
                        resenia.getCliente().getId(),
                        resenia.getCliente().getNombreYapellido(),  // AGREGAR NOMBRE DEL CLIENTE
                        resenia.getDescripcion(),
                        resenia.getPuntuacion()))
                .collect(Collectors.toList());

        List<DireccionDTO>direccionDTOS = restaurante.getDirecciones().stream().map(direccion ->
                new DireccionDTO(direccion.getId(), direccion.getDireccion(), direccion.getCiudad(), direccion.getPais(), direccion.getCodigoPostal())).collect(Collectors.toList());

        List<ComboResponseDTO> comboResponseDTOS = restaurante.getCombos().stream().map(combo -> new ComboResponseDTO(combo.getNombre(), combo.getProductos().stream().map(producto -> new ProductoResumenDTO(producto.getId(), producto.getNombre(), producto.getPrecio(), producto.getStock())).collect(Collectors.toSet())
                , combo.getDescuento(), combo.getPrecio())).collect(Collectors.toList());

        return new RestauranteDetailDTO(restaurante.getId(), restaurante.getNombre(), restaurante.getEmail(),
                menuDTO,comboResponseDTOS, reseniaDTO, direccionDTOS);
    }

    public Set<RestauranteResponseDTO> findAllRestaurantes(){
        List<Restaurante> lista = restauranteRepository.findAll();
        if (lista.isEmpty()) {
            throw new BusinessException("No hay restaurantes cargados actualmente");
        }
        return restauranteRepository.findAprobados().stream().map(r -> new RestauranteResponseDTO(r.getId(), r.getUsuario(), r.getNombre(), r.getEmail())).collect(Collectors.toSet());
    }

    public Set<RestauranteResponseDTO> findAllRestaurantesAdmin(){
        List<Restaurante> lista = restauranteRepository.findAll();
        if (lista.isEmpty()) {
            throw new BusinessException("No hay restaurantes cargados actualmente");
        }
        return restauranteRepository.findAll().stream().map(r -> new RestauranteResponseDTO(r.getId(),r.getUsuario(), r.getNombre(),r.getEmail())).collect(Collectors.toSet());
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

        Set<ProductoResumenDTO> productosResumen = productoSet.stream().map(producto -> new ProductoResumenDTO(producto.getId(),producto.getNombre(),producto.getPrecio(), producto.getStock())).collect(Collectors.toSet());

        return new ComboResponseDTO(combo.getNombre(), productosResumen, comboRequestDTO.getDescuento(), combo.getPrecio());
    }

    public List<ComboResponseDTO> verCombos(String usuario) {
        Restaurante restaurante = restauranteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        List<ComboResponseDTO> listaCombos = restaurante.getCombos().stream().map(combo -> {Set<ProductoResumenDTO> productosResumen = combo.getProductos().stream().map(producto -> new ProductoResumenDTO(producto.getId(), producto.getNombre(), producto.getPrecio(), producto.getStock())).collect(Collectors.toSet());
            return new ComboResponseDTO(combo.getNombre(), productosResumen, combo.getDescuento(), combo.getPrecio());}).collect(Collectors.toList());

        return listaCombos;
    }

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

        restauranteValidations.validarContraseniaActual(restaurante.getId(), contraseniaDTO.getContraseniaActual());

        if (!contraseniaDTO.getContraseniaNueva().equals(contraseniaDTO.getConfirmarContrasenia())) {
            throw new RuntimeException("Las contraseñas nuevas no coinciden");
        }

        restauranteValidations.validarContrasenia(contraseniaDTO.getContraseniaNueva());

        restaurante.setContrasenia(passwordEncoder.encode(contraseniaDTO.getContraseniaNueva()));
        restauranteRepository.save(restaurante);
    }
    public BalanceResponseDTO calcularBalance(String usuarioRestaurante, BalanceFiltroDTO filtro) {
        // Obtener el restaurante
        Restaurante restaurante = restauranteRepository.findByUsuario(usuarioRestaurante)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        List<Pedido> pedidosFiltrados;

        if ("dia".equals(filtro.getTipoFiltro()) && filtro.getFecha() != null) {
            // Filtrar por día específico - SOLO pedidos ENTREGADOS
            LocalDate fecha = LocalDate.parse(filtro.getFecha());
            pedidosFiltrados = pedidoRepository.findByRestauranteAndEstadoAndFechaPedidoBetween(
                    restaurante, EstadoPedido.ENTREGADO,
                    fecha.atStartOfDay(),
                    fecha.plusDays(1).atStartOfDay()
            );
        } else if ("mes".equals(filtro.getTipoFiltro()) && filtro.getMes() != null) {
            // Filtrar por mes - SOLO pedidos ENTREGADOS
            String[] partes = filtro.getMes().split("-");
            int year = Integer.parseInt(partes[0]);
            int month = Integer.parseInt(partes[1]);

            LocalDate inicioMes = LocalDate.of(year, month, 1);
            LocalDate finMes = inicioMes.plusMonths(1);

            pedidosFiltrados = pedidoRepository.findByRestauranteAndEstadoAndFechaPedidoBetween(
                    restaurante, EstadoPedido.ENTREGADO,
                    inicioMes.atStartOfDay(),
                    finMes.atStartOfDay()
            );
        } else {
            return new BalanceResponseDTO(0.0, 0, 0.0);
        }

        // Calcular totales
        Double totalRecaudado = pedidosFiltrados.stream()
                .mapToDouble(Pedido::getTotal)
                .sum();

        Integer cantidadPedidos = pedidosFiltrados.size();

        Double promedioVenta = cantidadPedidos > 0 ? totalRecaudado / cantidadPedidos : 0.0;

        return new BalanceResponseDTO(totalRecaudado, cantidadPedidos, promedioVenta);
    }

    // Obtener pendientes
    public List<RestauranteEstadoDTO> getRestaurantesPendientes() {
        return restauranteRepository.findByEstado(EstadoRestaurante.PENDIENTE)
                .stream()
                .map(this::toEstadoDTO)
                .toList();
    }

    public List<RestauranteEstadoDTO> getRestaurantesRechazados() {
        return restauranteRepository.findByEstado(EstadoRestaurante.RECHAZADO)
                .stream()
                .map(this::toEstadoDTO)
                .toList();
    }

    // Aprobar
    @Transactional
    public RestauranteEstadoDTO aprobarRestaurante(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        if (restaurante.getEstado() != EstadoRestaurante.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden aprobar restaurantes pendientes");
        }

        restaurante.setEstado(EstadoRestaurante.APROBADO);
        restaurante.setMotivoRechazo(null); // Limpiar si había rechazo previo

        Restaurante saved = restauranteRepository.save(restaurante);

        return toEstadoDTO(saved);
    }

    // Rechazar
    @Transactional
    public RestauranteEstadoDTO rechazarRestaurante(Long id, RechazarRestauranteDTO dto) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        if (restaurante.getEstado() != EstadoRestaurante.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden rechazar restaurantes pendientes");
        }

        restaurante.setEstado(EstadoRestaurante.RECHAZADO);
        restaurante.setMotivoRechazo(dto.motivoRechazo());

        Restaurante saved = restauranteRepository.save(restaurante);

        return toEstadoDTO(saved);
    }

    // Contador para badge en UI
    public long countPendientes() {
        return restauranteRepository.countByEstado(EstadoRestaurante.PENDIENTE);
    }

    private RestauranteEstadoDTO toEstadoDTO(Restaurante r) {
        return new RestauranteEstadoDTO(
                r.getId(),
                r.getUsuario(),
                r.getNombre(),
                r.getEstado(),
                r.getMotivoRechazo()
        );
    }

    public RestauranteEstadoDTO verEstado (String usuario){

       Restaurante restaurante = restauranteRepository.findByUsuario(usuario).orElseThrow(()-> new RuntimeException("Restaurante no encontrado"));

        return new RestauranteEstadoDTO(
                restaurante.getId(),
                restaurante.getUsuario(),
                restaurante.getNombre(),
                restaurante.getEstado(),
                restaurante.getMotivoRechazo());
    }

}