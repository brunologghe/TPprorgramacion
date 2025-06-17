package com.example.pedidosYA.Controller;

import com.example.pedidosYA.DTO.ClienteDTO.ClienteCrearDTO;
import com.example.pedidosYA.DTO.ClienteDTO.ClienteDetailDto;
import com.example.pedidosYA.DTO.ClienteDTO.ResponseDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoCreateDTO;
import com.example.pedidosYA.DTO.PedidoDTO.PedidoDetailDTO;
import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaCreateDTO;
import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaDetailDTO;
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
    public ResponseEntity<ResponseDTO> crear (@Valid @RequestBody ClienteCrearDTO cliente)
    {
        ResponseDTO bodyCliente = clienteService.crearUsuario(cliente);

        return ResponseEntity.status(HttpStatus.CREATED).body(bodyCliente);
    }

    @GetMapping("/{id}")
    public ClienteDetailDto verCliente(@PathVariable Long id)
    {
        return clienteService.verUsuario(id);
    }

    @PostMapping("/pedir/{id}")
    public ResponseEntity<PedidoDetailDTO> hacerPedido(@PathVariable Long id, @Valid @RequestBody PedidoCreateDTO pedido)
    {
        PedidoDetailDTO pedidoDetailDTO = pedidoService.hacerPedido(id, pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoDetailDTO);
    }

    @PostMapping("/resenia/{id}")
    public ResponseEntity<ReseniaDetailDTO> hacerResenia(@PathVariable Long id, @Valid @RequestBody ReseniaCreateDTO reseniaCreateDTO)
    {
        ReseniaDetailDTO reseniaDetailDTO = reseniaService.crearResenia(id, reseniaCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(reseniaDetailDTO);
    }

    @GetMapping("/pedidosEnCurso/{idCliente}")
    public List<PedidoDetailDTO> verPedidosEnCurso(@PathVariable Long idCliente)
    {
        return pedidoService.verPedidosEnCurso(idCliente);
    }

    @GetMapping("/historialPedidos/{idCliente}")
    public List<PedidoDetailDTO> verHistorialPedidos(@PathVariable Long idCliente){
        return pedidoService.verHistorialPedidos(idCliente);
    }

    @GetMapping("/verDetallesPedido/{idPedido}")
    public PedidoDetailDTO verDetallesPedido(@PathVariable Long idPedido){
        return pedidoService.verDetallesPedido(idPedido);
    }

    @DeleteMapping("/{idPedido}")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long idPedido){
        pedidoService.cancelarPedido(idPedido);
        return ResponseEntity.status(HttpStatus.OK).body("Pedido con id: "+idPedido+" eliminado");
    }
}
