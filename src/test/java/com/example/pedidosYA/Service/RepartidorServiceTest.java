package com.example.pedidosYA.Service;

import com.example.pedidosYA.Model.EstadoPedido;
import com.example.pedidosYA.Model.Pedido;
import com.example.pedidosYA.Model.Repartidor;
import com.example.pedidosYA.Repository.PedidoRepository;
import com.example.pedidosYA.Repository.RepartidorRepository;
import com.example.pedidosYA.Repository.ReseniaRepository;
import com.example.pedidosYA.Validations.RepartidorValidations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepartidorServiceTest {

    @Mock
    private RepartidorRepository repartidorRepository;
    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private ReseniaRepository reseniaRepository;
    @Mock
    private RepartidorValidations repartidorValidations;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RepartidorService repartidorService;

    @Test
    void tomarPedido_noCambiaDisponibilidadAutomaticamente() {
        Repartidor repartidor = new Repartidor();
        repartidor.setId(1L);
        repartidor.setUsuario("repartidor1");
        repartidor.setDisponible(true);

        Pedido pedido = new Pedido();
        pedido.setId(10L);
        pedido.setEstado(EstadoPedido.PREPARACION);

        when(repartidorRepository.findByUsuario("repartidor1")).thenReturn(Optional.of(repartidor));
        when(pedidoRepository.findById(10L)).thenReturn(Optional.of(pedido));

        repartidorService.tomarPedido("repartidor1", 10L);

        assertEquals(EstadoPedido.ENVIADO, pedido.getEstado());
        assertNotNull(pedido.getRepartidor());
        assertEquals(1L, pedido.getRepartidor().getId());
        assertEquals(true, repartidor.getDisponible());

        verify(repartidorValidations).validarDisponible(1L);
        verify(repartidorValidations).validarNoTienePedidoEnCurso(1L);
        verify(repartidorValidations).validarPedidoDisponible(10L);
        verify(pedidoRepository).save(pedido);
        verify(repartidorRepository).save(repartidor);
    }
}
