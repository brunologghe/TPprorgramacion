package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.PedidoDTO.DetallePedidoDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoCreateDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoDetailDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoResumenDTO;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.*;
import com.example.pedidosYA.Repository.ClienteRepository;
import com.example.pedidosYA.Repository.PedidoRepository;
import com.example.pedidosYA.Repository.ProductoRepository;
import com.example.pedidosYA.Repository.RestauranteRepository;
import com.example.pedidosYA.Validations.ClienteValidations;
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

    public PedidoDetailDTO hacerPedido(Long idCliente, PedidoCreateDTO pedidoCreateDTO)
    {
        Cliente cliente = clienteValidations.validarExistencia(idCliente);
        Restaurante restaurante =  restauranteValidations.validarExisteId(pedidoCreateDTO.getRestauranteId());
        clienteValidations.validarDireccion(pedidoCreateDTO.getDireccionId(), idCliente);
        Pedido pedido = new Pedido();
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PREPARACION);

        double total = 0;
        List<ProductoPedido> productosPedido = new ArrayList<>();

        for(DetallePedidoDTO dpdto : pedidoCreateDTO.getDetalles())
        {
            Producto producto = productoRepository.findById(dpdto.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto con ID: " +dpdto.getProductoId()+ "no encontrado"));

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
                pedidohecho.getTotal(), pedidohecho.getRestaurante().getNombre(), idCliente, pedidoCreateDTO.getDetalles());
    }

    public List<PedidoDetailDTO> verPedidosEnCurso(Long idCliente)
    {
        Cliente cliente = clienteValidations.validarExistencia(idCliente);

        List<Pedido>listaPedidos = cliente.getPedidos();
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

        if(listaDetallePedidos.isEmpty())
        {
            throw new BusinessException("No hay pedidos en curso");
        }

        return listaDetallePedidos;
    }

    public List<PedidoDetailDTO> verHistorialPedidos(Long idCliente)
    {
        Cliente cliente = clienteValidations.validarExistencia(idCliente);

        List<Pedido>listaPedidos = cliente.getPedidos();
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

        if(listaDetallePedidos.isEmpty())
        {
            throw new BusinessException("No hay pedidos en el historial aun");
        }

        return listaDetallePedidos;
    }

    public PedidoDetailDTO verDetallesPedido(Long idPedido){

        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(()-> new BusinessException("Ese pedido no existe"));

        List<DetallePedidoDTO>detallePedido = new ArrayList<>();
        for(ProductoPedido p : pedido.getProductosPedidos())
        {
            detallePedido.add(new DetallePedidoDTO(p.getProducto().getId(), p.getCantidad()));
        }

        return new PedidoDetailDTO(pedido.getId(), pedido.getFechaPedido(), pedido.getEstado(), pedido.getTotal(), pedido.getRestaurante().getNombre(), pedido.getCliente().getId(), detallePedido);
    }

    public void cancelarPedido(Long idPedido){
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(()-> new BusinessException("Ese pedido no existe"));

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

    public List<PedidoResumenDTO> verPedidosDeRestauranteEnCurso (Long idRestaurante){
        Restaurante restaurante = restauranteRepository.findById(idRestaurante)
                .orElseThrow(() -> new BusinessException("No existe ningún restaurante con ese id"));

        return pedidoRepository.findByRestauranteId(idRestaurante).stream()
                .filter(pedido -> pedido.getEstado() == EstadoPedido.PREPARACION || pedido.getEstado() == EstadoPedido.ENVIADO)
                .map(pedido -> new PedidoResumenDTO(pedido.getId(), pedido.getFechaPedido(),
                        pedido.getEstado().toString(), pedido.getTotal())).toList();
    }

    public List<PedidoResumenDTO> verHistorialPedidosDeRestaurante (Long idRestaurante){
        Restaurante restaurante = restauranteRepository.findById(idRestaurante)
                .orElseThrow(() -> new BusinessException("No existe ningún restaurante con ese id"));

        return pedidoRepository.findByRestauranteId(idRestaurante).stream()
                .filter(pedido -> pedido.getEstado() == EstadoPedido.ENTREGADO || pedido.getEstado() == EstadoPedido.PREPARACION || pedido.getEstado() == EstadoPedido.ENVIADO)
                .map(pedido -> new PedidoResumenDTO(pedido.getId(), pedido.getFechaPedido(),
                        pedido.getEstado().toString(), pedido.getTotal())).toList();
    }
}
