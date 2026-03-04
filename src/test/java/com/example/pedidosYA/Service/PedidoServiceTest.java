package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.PedidoDTO.PedidoDetailDTO;
import com.example.pedidosYA.Model.*;
import com.example.pedidosYA.Repository.*;
import com.example.pedidosYA.Validations.ClienteValidations;
import com.example.pedidosYA.Validations.PedidoValidations;
import com.example.pedidosYA.Validations.RestauranteValidations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private RestauranteRepository restauranteRepository;
    @Mock
    private ClienteValidations clienteValidations;
    @Mock
    private RestauranteValidations restauranteValidations;
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private PedidoValidations pedidoValidations;
    @Mock
    private TarjetaRepository pagoRepository;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void verPedidosDeRestauranteEnCurso_incluyeRepartidorEnEstadoEnviado() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(10L);
        restaurante.setNombre("Resto Test");
        restaurante.setUsuario("resto_test");

        Cliente cliente = new Cliente();
        cliente.setId(20L);

        Repartidor repartidor = new Repartidor();
        repartidor.setId(30L);
        repartidor.setNombreYapellido("Juan Repartidor");
        repartidor.setEmail("juan@correo.com");
        repartidor.setPais("AR");
        repartidor.setTipoVehiculo(TipoVehiculo.BICICLETA);

        Producto producto = new Producto();
        producto.setId(40L);
        producto.setNombre("Pizza");
        producto.setPrecio(1000.0);

        ProductoPedido productoPedido = new ProductoPedido();
        productoPedido.setProducto(producto);
        productoPedido.setCantidad(2);

        Pedido pedido = new Pedido();
        pedido.setId(50L);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.ENVIADO);
        pedido.setTotal(2000.0);
        pedido.setRestaurante(restaurante);
        pedido.setCliente(cliente);
        pedido.setRepartidor(repartidor);
        pedido.setProductosPedidos(List.of(productoPedido));

        when(restauranteRepository.findByUsuario("resto_test")).thenReturn(Optional.of(restaurante));
        when(pedidoRepository.findByRestauranteId(10L)).thenReturn(List.of(pedido));

        List<PedidoDetailDTO> resultado = pedidoService.verPedidosDeRestauranteEnCurso("resto_test");

        assertEquals(1, resultado.size());
        PedidoDetailDTO dto = resultado.get(0);
        assertEquals(EstadoPedido.ENVIADO, dto.estado());
        assertNotNull(dto.repartidor());
        assertEquals(30L, dto.repartidor().id());
        assertEquals("Juan Repartidor", dto.repartidor().nombreYapellido());
    }
}
