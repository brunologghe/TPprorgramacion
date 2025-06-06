package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.PedidoDTO.DetallePedidoDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoCreateDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoDetailDTO;
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

    public PedidoDetailDTO pedirPedido(Long idCliente, PedidoCreateDTO pedidoCreateDTO)
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
        pedido.setDetalles();
        pedido.setTotal(total);
        pedido.setRestaurante(restaurante);
        pedido.setCliente(cliente);

        Pedido pedidohecho = pedidoRepository.save(pedido);

        return new PedidoDetailDTO(pedidohecho.getId(), pedidohecho.getFechaPedido(), pedidohecho.getEstado(),
                pedidohecho.getTotal(), pedidohecho.getRestaurante().getNombre(), idCliente, pedidoCreateDTO.getDetalles());
    }
}
