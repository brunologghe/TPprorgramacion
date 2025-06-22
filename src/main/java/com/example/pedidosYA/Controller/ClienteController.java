
package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.ClienteDTO.ClienteCrearDTO;
import com.example.pedidosYA.DTO.ClienteDTO.ClienteDetailDto;
import com.example.pedidosYA.DTO.ClienteDTO.ResponseDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoCreateDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoDetailDTO;
import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaCreateDTO;
import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaDetailDTO;
import com.example.pedidosYA.Security.AuthUtil;
import com.example.pedidosYA.Service.ClienteService;
import com.example.pedidosYA.Service.PedidoService;
import com.example.pedidosYA.Service.ReseniaService;
import jakarta.persistence.DiscriminatorValue;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
@DiscriminatorValue("CLIENTE")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ReseniaService reseniaService;

    @PostMapping
    public ResponseEntity<ResponseDTO> crear (@Valid @RequestBody ClienteCrearDTO cliente) {
        ResponseDTO bodyCliente = clienteService.crearUsuario(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(bodyCliente);
    }

    @GetMapping("/perfil")
    @PreAuthorize("hasRole('CLIENTE')")
    public ClienteDetailDto verCliente() {
        String usuario = AuthUtil.getUsuarioLogueado();
        return clienteService.verUsuarioPorNombre(usuario);
    }

    @PostMapping("/pedir")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<PedidoDetailDTO> hacerPedido(@Valid @RequestBody PedidoCreateDTO pedido) {
        String usuario = AuthUtil.getUsuarioLogueado();
        PedidoDetailDTO pedidoDetailDTO = pedidoService.hacerPedido(usuario, pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoDetailDTO);
    }

    @PostMapping("/resenia")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ReseniaDetailDTO> hacerResenia(@Valid @RequestBody ReseniaCreateDTO reseniaCreateDTO) {
        String usuario = AuthUtil.getUsuarioLogueado();
        ReseniaDetailDTO reseniaDetailDTO = reseniaService.crearResenia(usuario, reseniaCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(reseniaDetailDTO);
    }

    @GetMapping("/pedidosEnCurso")
    @PreAuthorize("hasRole('CLIENTE')")
    public List<PedidoDetailDTO> verPedidosEnCurso() {
        String usuario = AuthUtil.getUsuarioLogueado();
        return pedidoService.verPedidosEnCurso(usuario);
    }

    @GetMapping("/historialPedidos")
    @PreAuthorize("hasRole('CLIENTE')")
    public List<PedidoDetailDTO> verHistorialPedidos() {
        String usuario = AuthUtil.getUsuarioLogueado();
        return pedidoService.verHistorialPedidos(usuario);
    }

    @GetMapping("/verDetallesPedido/{idPedido}")
    @PreAuthorize("hasRole('CLIENTE')")
    public PedidoDetailDTO verDetallesPedido(@PathVariable Long idPedido) {
        return pedidoService.verDetallesPedido(idPedido);
    }

    @DeleteMapping("/{idPedido}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long idPedido) {
        pedidoService.cancelarPedido(AuthUtil.getUsuarioLogueado(),idPedido);
        return ResponseEntity.status(HttpStatus.OK).body("Pedido con id: "+idPedido+" eliminado");
    }
}
