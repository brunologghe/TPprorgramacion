package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.PedidoDTO.DetallePedidoDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoCreateDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoDetailDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoResumenDTO;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.*;
import com.example.pedidosYA.Repository.*;
import com.example.pedidosYA.Validations.ClienteValidations;
import com.example.pedidosYA.Validations.PedidoValidations;
import com.example.pedidosYA.Validations.RestauranteValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private PagoRepository pagoRepository;

    public PedidoDetailDTO hacerPedido(String usuario, PedidoCreateDTO pedidoCreateDTO) {
        Cliente cliente = clienteRepository.findByUsuario(usuario);
        Restaurante restaurante =  restauranteValidations.validarExisteId(pedidoCreateDTO.getRestauranteId());
        clienteValidations.validarDireccion(pedidoCreateDTO.getDireccionId(), cliente.getId());
        Pago metodoPago = pagoRepository.findById(pedidoCreateDTO.getPagoId())
                .orElseThrow(() -> new BusinessException("Método de pago no encontrado"));
        if (!metodoPago.getCliente().getId().equals(cliente.getId())) {
            throw new BusinessException("El método de pago no pertenece al cliente autenticado");
        }

        Pedido pedido = new Pedido();
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PREPARACION);

        double total = 0;
        List<ProductoPedido> productosPedido = new ArrayList<>();

        for(DetallePedidoDTO dpdto : pedidoCreateDTO.getDetalles())
        {
            Producto producto = productoRepository.findById(dpdto.getProductoId())
                    .orElseThrow(() -> new BusinessException("Producto con ID: " + dpdto.getProductoId() + " no encontrado"));

            pedidoValidations.verificarCantidadPedido(dpdto.getCantidad());

            ProductoPedido productoPedido = new ProductoPedido();
            productoPedido.setProducto(producto);
            productoPedido.setCantidad(dpdto.getCantidad());
            productosPedido.add(productoPedido);

            double subtotal = producto.getPrecio() * dpdto.getCantidad();
            total += subtotal;
        }
        pedido.setTotal(total);
        pedido.setRestaurante(restaurante);
        pedido.setCliente(cliente);
        pedido.setProductosPedidos(productosPedido);

        Pedido pedidohecho = pedidoRepository.save(pedido);

        return new PedidoDetailDTO(pedidohecho.getId(), pedidohecho.getFechaPedido(), pedidohecho.getEstado(),
             pedidohecho.getTotal(), pedidohecho.getRestaurante().getNombre(), cliente.getId(), pedidoCreateDTO.getDetalles());

    }

    public List<PedidoDetailDTO> verPedidosEnCurso(String usuario) {
        Cliente cliente = clienteRepository.findByUsuario(usuario);
        clienteValidations.validarExistencia(cliente.getId());

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
        Cliente cliente = clienteRepository.findByUsuario(usuario);
        clienteValidations.validarExistencia(cliente.getId());


        List<PedidoDetailDTO>listaDetallePedidos = new ArrayList<>();

        List<DetallePedidoDTO> detalles = new ArrayList<>();
        for(Pedido d : cliente.getPedidos())
        {
                for(ProductoPedido p : d.getProductosPedidos())
                {
                    DetallePedidoDTO detallePedidoDTO = new DetallePedidoDTO();
                    detallePedidoDTO.setProductoId(p.getProducto().getId());
                    detallePedidoDTO.setCantidad(p.getCantidad());
                    detalles.add(detallePedidoDTO);
                }
                listaDetallePedidos.add(new PedidoDetailDTO(d.getId(), d.getFechaPedido(), d.getEstado(), d.getTotal(), d.getRestaurante().getNombre(), d.getCliente().getId(), detalles));
        }

        pedidoValidations.verificarPedidoDetailDTO(listaDetallePedidos);


        return listaDetallePedidos;
    }

    public PedidoDetailDTO verDetallesPedido(Long idPedido){

        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(()-> new BusinessException("Ese pedido no existe"));

        List<DetallePedidoDTO>detallePedido = new ArrayList<>();
        for(ProductoPedido p : pedido.getProductosPedidos())
        {
            detallePedido.add(new DetallePedidoDTO(p.getProducto().getId(), p.getCantidad()));
        }

        pedidoValidations.verificarDetallesPedido(detallePedido);

        return new PedidoDetailDTO(pedido.getId(), pedido.getFechaPedido(), pedido.getEstado(), pedido.getTotal(), pedido.getRestaurante().getNombre(), pedido.getCliente().getId(), detallePedido);
    }

    public void cancelarPedido(String usuario, Long idPedido){
        Cliente cliente = clienteRepository.findByUsuario(usuario);
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(()-> new BusinessException("Ese pedido no existe"));

        if(!cliente.getId().equals(pedido.getCliente().getId())){
            throw new BusinessException("El pedido no es del cliente");
        }

        pedidoRepository.delete(pedido);
    }


    public PedidoDetailDTO modificarEstadoPedido (Long idPedido, String estado){

        EstadoPedido estadoPedido = EstadoPedido.valueOf(estado);
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(()-> new BusinessException("Ese pedido no existe"));

        pedido.setEstado(estadoPedido);

        pedidoRepository.save(pedido);

        List<DetallePedidoDTO>detallePedido = new ArrayList<>();
        for(ProductoPedido p : pedido.getProductosPedidos())
        {
            detallePedido.add(new DetallePedidoDTO(p.getProducto().getId(), p.getCantidad()));
        }

        return new PedidoDetailDTO(pedido.getId(), pedido.getFechaPedido(), pedido.getEstado(), pedido.getTotal(), pedido.getRestaurante().getNombre(), pedido.getCliente().getId(), detallePedido);
    }

    public List<PedidoResumenDTO> verPedidosDeRestauranteEnCurso (String usuario){
        Restaurante restaurante = restauranteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("No existe ningún restaurante con ese nombre"));

        return pedidoRepository.findByRestauranteId(restaurante.getId()).stream()
                .filter(pedido -> pedido.getEstado() == EstadoPedido.PREPARACION || pedido.getEstado() == EstadoPedido.ENVIADO)
                .map(pedido -> new PedidoResumenDTO(pedido.getId(), pedido.getFechaPedido(),
                        pedido.getEstado().toString(), pedido.getTotal())).toList();
    }

    public List<PedidoResumenDTO> verHistorialPedidosDeRestaurante (String usuario){
        Restaurante restaurante = restauranteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("No existe ningún restaurante con ese nombre"));

        return pedidoRepository.findByRestauranteId(restaurante.getId()).stream()
                .filter(pedido -> pedido.getEstado() == EstadoPedido.ENTREGADO || pedido.getEstado() == EstadoPedido.PREPARACION || pedido.getEstado() == EstadoPedido.ENVIADO)
                .map(pedido -> new PedidoResumenDTO(pedido.getId(), pedido.getFechaPedido(),
                        pedido.getEstado().toString(), pedido.getTotal())).toList();
    }
}
