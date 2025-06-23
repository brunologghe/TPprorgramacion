
package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.ClienteDTO.ClienteCrearDTO;
import com.example.pedidosYA.DTO.ClienteDTO.ClienteDetailDto;
import com.example.pedidosYA.DTO.ClienteDTO.ModificarDTO;
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


    @GetMapping("/perfil")
    @PreAuthorize("hasRole('CLIENTE')")
    public ClienteDetailDto verCliente() {
        return clienteService.verUsuarioPorNombre(AuthUtil.getUsuarioLogueado());
    }

    @PutMapping("/perfil")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> modificarUsuarioNombreCliente (@Valid @RequestBody ModificarDTO modificarDTO){
        clienteService.modificarUsuarioNombre(AuthUtil.getUsuarioLogueado(), modificarDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario y/o Nombre cambiados con exito!");
    }

    @PutMapping("/contrasenia")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> modificarContraseniaCliente (@Valid @RequestBody ModificarDTO modificarDTO){
        clienteService.modificarContrasenia(AuthUtil.getUsuarioLogueado(), modificarDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Contrasenia cambiada con exito!");
    }

    @PostMapping("/pedir")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<PedidoDetailDTO> hacerPedido(@Valid @RequestBody PedidoCreateDTO pedido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.hacerPedido(AuthUtil.getUsuarioLogueado(), pedido));
    }

    @PostMapping("/resenia")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ReseniaDetailDTO> hacerResenia(@Valid @RequestBody ReseniaCreateDTO reseniaCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reseniaService.crearResenia(AuthUtil.getUsuarioLogueado(), reseniaCreateDTO));
    }

    @GetMapping("/pedidosEnCurso")
    @PreAuthorize("hasRole('CLIENTE')")
    public List<PedidoDetailDTO> verPedidosEnCurso() {
        return pedidoService.verPedidosEnCurso(AuthUtil.getUsuarioLogueado());
    }

    @GetMapping("/historialPedidos")
    @PreAuthorize("hasRole('CLIENTE')")
    public List<PedidoDetailDTO> verHistorialPedidos() {
        return pedidoService.verHistorialPedidos(AuthUtil.getUsuarioLogueado());
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
