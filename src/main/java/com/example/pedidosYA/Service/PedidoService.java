package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.PedidoDTO.DetallePedidoDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoCreateDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoDetailDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoResumenDTO;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.*;
import com.example.pedidosYA.Repository.*;
import com.example.pedidosYA.Security.AuthUtil;
import com.example.pedidosYA.Validations.ClienteValidations;
import com.example.pedidosYA.Validations.PedidoValidations;
import com.example.pedidosYA.Validations.RestauranteValidations;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.pedidosYA.Service.EmailService;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private ClienteValidations clienteValidations;
    @Autowired
    private RestauranteValidations restauranteValidations;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private PedidoValidations pedidoValidations;
    @Autowired
    private TarjetaRepository pagoRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public PedidoDetailDTO hacerPedido(String usuario, PedidoCreateDTO pedidoCreateDTO) {
        Cliente cliente = clienteRepository.findByUsuario(usuario).orElseThrow(() -> new BusinessException("Cliente no encontrado"));
        Restaurante restaurante =  restauranteValidations.validarExisteId(pedidoCreateDTO.getRestauranteId());
        // Validar que la dirección del restaurante exista y pertenezca al restaurante
        Direccion direccionRestaurante = restaurante.getDirecciones().stream()
                .filter(dir -> dir.getId().equals(pedidoCreateDTO.getDireccionRestauranteId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("La dirección del restaurante no existe o no pertenece a este restaurante"));



        clienteValidations.validarDireccion(pedidoCreateDTO.getDireccionId(), cliente.getId());
        Direccion direccionEntrega = cliente.getDirecciones().stream()
                .filter(dir -> dir.getId().equals(pedidoCreateDTO.getDireccionId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("La dirección de entrega no existe o no pertenece al cliente"));
        
        Tarjeta metodoPago = pagoRepository.findById(pedidoCreateDTO.getPagoId())
                .orElseThrow(() -> new BusinessException("Método de pago no encontrado"));
        if (!metodoPago.getCliente().getId().equals(cliente.getId())) {
            throw new BusinessException("El método de pago no pertenece al cliente autenticado");
        }

        Pedido pedido = new Pedido();
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setDireccionRestaurante(direccionRestaurante);
        pedido.setDireccionEntrega(direccionEntrega);
        double total = 0;
        List<ProductoPedido> productosPedido = new ArrayList<>();

        for(DetallePedidoDTO dpdto : pedidoCreateDTO.getDetalles())
        {
            Producto producto = productoRepository.findById(dpdto.getProductoId())
                    .orElseThrow(() -> new BusinessException("Producto con ID: " + dpdto.getProductoId() + " no encontrado"));

            pedidoValidations.verificarCantidadPedido(dpdto.getCantidad());
            pedidoValidations.verificarStockProducto(producto);
            ProductoPedido productoPedido = new ProductoPedido();
            productoPedido.setProducto(producto);
            productoPedido.setCantidad(dpdto.getCantidad());
            productosPedido.add(productoPedido);
            productoPedido.setPedido(pedido);

            double subtotal = producto.getPrecio() * dpdto.getCantidad();
            total += subtotal;
            productoPedido.getProducto().setStock(productoPedido.getProducto().getStock() - dpdto.getCantidad());
        }
        pedido.setTotal(total);
        pedido.setRestaurante(restaurante);
        pedido.setCliente(cliente);
        pedido.setProductosPedidos(productosPedido);

        Pedido pedidohecho = pedidoRepository.save(pedido);

        try {
            String emailRestaurante = restaurante.getEmail();
            String nombreCliente = cliente.getNombreYapellido() != null ? cliente.getNombreYapellido() : cliente.getUsuario();
            emailService.enviarEmailNuevoPedidoRestaurante(emailRestaurante, pedidohecho.getId(), nombreCliente, pedidohecho.getTotal());

            String emailCliente = cliente.getEmail();
            emailService.enviarEmailPedidoConfirmado(emailCliente, pedidohecho.getId(), pedidohecho.getTotal());
        } catch (Exception e) {
            // Log error pero no interrumpir el flujo del pedido
        }

        List<DetallePedidoDTO> detalles = new ArrayList<>();
        for (ProductoPedido pp : pedidohecho.getProductosPedidos()) {
            detalles.add(new DetallePedidoDTO(
                    pp.getProducto().getId(),
                    pp.getProducto().getNombre(),
                    pp.getProducto().getPrecio(),
                    pp.getCantidad()
            ));
        }
        return new PedidoDetailDTO(pedidohecho.getId(), pedidohecho.getFechaPedido(), pedidohecho.getEstado(),
        pedidohecho.getTotal(), pedidohecho.getRestaurante().getNombre(), cliente.getId(), detalles);

    }

    public List<PedidoDetailDTO> verPedidosEnCurso(String usuario) {
        Cliente cliente = clienteRepository.findByUsuario(usuario).orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        List<PedidoDetailDTO>listaDetallePedidos = new ArrayList<>();

        for(Pedido d : cliente.getPedidos())
        {
            if(d.getEstado().equals(EstadoPedido.ENVIADO) || d.getEstado().equals(EstadoPedido.PREPARACION))
            {
                List<DetallePedidoDTO> detalles = new ArrayList<>();
                for(ProductoPedido p : d.getProductosPedidos())
                {
                    DetallePedidoDTO detallePedidoDTO = new DetallePedidoDTO();
                    detallePedidoDTO.setProductoId(p.getProducto().getId());
                    detallePedidoDTO.setCantidad(p.getCantidad());
                    detalles.add(detallePedidoDTO);
                }
                listaDetallePedidos.add(new PedidoDetailDTO(d.getId(), d.getFechaPedido(), d.getEstado(), d.getTotal(), d.getRestaurante().getNombre(), d.getCliente().getId(), detalles));
            }
        }

        pedidoValidations.verificarPedidoDetailDTO(listaDetallePedidos);

        return listaDetallePedidos;
    }

    public List<PedidoDetailDTO> verHistorialPedidos(String usuario) {
        Cliente cliente = clienteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado"));
        clienteValidations.validarExistencia(cliente.getId());

        List<PedidoDetailDTO> listaDetallePedidos = new ArrayList<>();

        for(Pedido pedido : cliente.getPedidos()) {
            // CREAR UNA NUEVA LISTA DE DETALLES PARA CADA PEDIDO
            List<DetallePedidoDTO> detalles = new ArrayList<>();

            for(ProductoPedido productoPedido : pedido.getProductosPedidos()) {
                DetallePedidoDTO detallePedidoDTO = new DetallePedidoDTO();
                detallePedidoDTO.setProductoId(productoPedido.getProducto().getId());
                detallePedidoDTO.setNombreProducto(productoPedido.getProducto().getNombre());
                detallePedidoDTO.setPrecioUnitario(productoPedido.getProducto().getPrecio());
                detallePedidoDTO.setCantidad(productoPedido.getCantidad());
                detalles.add(detallePedidoDTO);
            }

            listaDetallePedidos.add(new PedidoDetailDTO(
                    pedido.getId(),
                    pedido.getFechaPedido(),
                    pedido.getEstado(),
                    pedido.getTotal(),
                    pedido.getRestaurante().getNombre(),
                    pedido.getCliente().getId(),
                    detalles
            ));
        }

        pedidoValidations.verificarPedidoDetailDTO(listaDetallePedidos);

        return listaDetallePedidos;
    }

    public PedidoDetailDTO verDetallesPedido(Long idPedido) {
        String username = AuthUtil.getUsuarioLogueado();
        Cliente cliente = clienteRepository.findByUsuario(username).orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        Pedido pedido = pedidoRepository.findById(idPedido)
                .filter(p -> p.getCliente().getId().equals(cliente.getId()))
                .orElseThrow(() -> new BusinessException("Ese pedido no existe"));

        List<DetallePedidoDTO> detallePedido = new ArrayList<>();
        for (ProductoPedido p : pedido.getProductosPedidos()) {
            detallePedido.add(new DetallePedidoDTO(p.getProducto().getId(), p.getCantidad()));
        }

        pedidoValidations.verificarDetallesPedido(detallePedido);

        return new PedidoDetailDTO(
                pedido.getId(),
                pedido.getFechaPedido(),
                pedido.getEstado(),
                pedido.getTotal(),
                pedido.getRestaurante().getNombre(),
                pedido.getCliente().getId(),
                detallePedido
        );
    }

    @Transactional
    public void cancelarPedido(String usuario, Long idPedido){
        Cliente cliente = clienteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado"));
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(()-> new BusinessException("Ese pedido no existe"));

        if(!cliente.getId().equals(pedido.getCliente().getId())){
            throw new BusinessException("Ese pedido no te pertenece");
        }
        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new BusinessException("No se puede cancelar un pedido que ya fue tomado por el restaurante");
        }

        // Restaurar el stock de los productos
        for (ProductoPedido productoPedido : pedido.getProductosPedidos()) {
            Producto producto = productoPedido.getProducto();
            producto.setStock(producto.getStock() + productoPedido.getCantidad());
            productoRepository.save(producto);
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }

    @Transactional
    public PedidoDetailDTO modificarEstadoPedido(Long idPedido, String estado) {

        EstadoPedido estadoPedido;
        try {
            estadoPedido = EstadoPedido.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Ese estado no es válido");
        }

        String username = AuthUtil.getUsuarioLogueado();
        Restaurante restaurante = restauranteRepository.findByUsuario(username).orElseThrow();

        Pedido pedido = pedidoRepository.findById(idPedido)
                .filter(p -> p.getRestaurante().getId().equals(restaurante.getId()))
                .orElseThrow(() -> new BusinessException("Ese pedido no existe"));

        if (estadoPedido == EstadoPedido.CANCELADO && pedido.getEstado() != EstadoPedido.CANCELADO) {
            for (ProductoPedido productoPedido : pedido.getProductosPedidos()) {
                Producto producto = productoPedido.getProducto();
                producto.setStock(producto.getStock() + productoPedido.getCantidad());
                productoRepository.save(producto);
            }
        }

        pedido.setEstado(estadoPedido);
        pedidoRepository.save(pedido);

        // Notificar al cliente del cambio de estado (asincrónico)
        try {
            String emailCliente = pedido.getCliente().getEmail();
            emailService.enviarEmailCambioEstado(emailCliente, pedido.getId(), pedido.getEstado().name());
        } catch (Exception e) {
            // No interrumpir flujo por errores de notificación
        }

        List<DetallePedidoDTO> detallePedido = new ArrayList<>();
        for (ProductoPedido p : pedido.getProductosPedidos()) {
            detallePedido.add(new DetallePedidoDTO(p.getProducto().getId(), p.getCantidad()));
        }

        return new PedidoDetailDTO(
                pedido.getId(),
                pedido.getFechaPedido(),
                pedido.getEstado(),
                pedido.getTotal(),
                pedido.getRestaurante().getNombre(),
                pedido.getCliente().getId(),
                detallePedido
        );
    }


    public List<PedidoDetailDTO> verPedidosDeRestauranteEnCurso (String usuario){
        Restaurante restaurante = restauranteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("No existe ningún restaurante con ese nombre"));

        return pedidoRepository.findByRestauranteId(restaurante.getId()).stream()
                .filter(pedido ->
                        pedido.getEstado() == EstadoPedido.PENDIENTE
                                || pedido.getEstado() == EstadoPedido.PREPARACION
                                || pedido.getEstado() == EstadoPedido.ENVIADO
                )
                .map(pedido -> new PedidoDetailDTO(
                        pedido.getId(),
                        pedido.getFechaPedido(),
                        pedido.getEstado(),
                        pedido.getTotal(),
                        pedido.getRestaurante().getNombre(),
                        pedido.getCliente().getId(),
                        pedido.getProductosPedidos().stream()
                                .map(pp -> new DetallePedidoDTO(
                                        pp.getProducto().getId(),
                                        pp.getProducto().getNombre(),
                                        pp.getProducto().getPrecio(),
                                        pp.getCantidad()
                                ))
                                .toList()
                ))
                .toList();
    }

    public List<PedidoDetailDTO> verHistorialPedidosDeRestaurante (String usuario){
        Restaurante restaurante = restauranteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("No existe ningún restaurante con ese nombre"));

        return pedidoRepository.findByRestauranteId(restaurante.getId()).stream()
                .filter(pedido -> pedido.getEstado() == EstadoPedido.ENTREGADO || pedido.getEstado() == EstadoPedido.PREPARACION || pedido.getEstado() == EstadoPedido.ENVIADO)
                .map(pedido -> new PedidoDetailDTO(pedido.getId(), pedido.getFechaPedido(),
                        pedido.getEstado(),  pedido.getTotal(), pedido.getRestaurante().getNombre(), pedido.getCliente().getId(),
                        pedido.getProductosPedidos().stream()
                        .map(productoPedido -> new DetallePedidoDTO(
                                productoPedido.getProducto().getId(),
                                productoPedido.getProducto().getNombre(),
                                productoPedido.getProducto().getPrecio(),
                                productoPedido.getCantidad()
                        )).toList())).toList();
    }

    public List<PedidoDetailDTO> verPedidosCompleto(String usuario) {
        Restaurante restaurante = restauranteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("No existe ningún restaurante con ese nombre"));

        return pedidoRepository.findByRestauranteId(restaurante.getId()).stream()
                .map(pedido -> {
                    List<DetallePedidoDTO> detalles = pedido.getProductosPedidos().stream()
                            .map(productoPedido -> new DetallePedidoDTO(
                                    productoPedido.getProducto().getId(),
                                    productoPedido.getProducto().getNombre(),
                                    productoPedido.getProducto().getPrecio(),
                                    productoPedido.getCantidad()
                            )).toList();

                    return new PedidoDetailDTO(
                            pedido.getId(),
                            pedido.getFechaPedido(),
                            pedido.getEstado(),
                            pedido.getTotal(),
                            restaurante.getNombre(),
                            pedido.getCliente().getId(),
                            detalles
                    );
                }).toList();
    }
}
