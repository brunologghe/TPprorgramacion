package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaCreateDTO;
import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaDetailDTO;
import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaResumenDTO;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Resenia;
import com.example.pedidosYA.Model.Restaurante;
import com.example.pedidosYA.Repository.ClienteRepository;
import com.example.pedidosYA.Repository.PedidoRepository;
import com.example.pedidosYA.Repository.ReseniaRepository;
import com.example.pedidosYA.Repository.RestauranteRepository;
import com.example.pedidosYA.Validations.ClienteValidations;
import com.example.pedidosYA.Validations.ReseniaValidations;
import com.example.pedidosYA.Validations.RestauranteValidations;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ReseniaService {

    @Autowired
    private ReseniaRepository reseniaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private RestauranteValidations restauranteValidations;
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private ReseniaValidations reseniaValidations;
    @Autowired
    private PedidoRepository pedidoRepository;

    @Transactional
    public ReseniaDetailDTO crearResenia(String usuario, ReseniaCreateDTO reseniaCreateDTO) {
        Cliente cliente = clienteRepository.findByUsuario(usuario).orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        Restaurante restaurante = restauranteValidations.validarExisteId(reseniaCreateDTO.getRestauranteId());

        boolean tienePedidos = pedidoRepository.findByRestauranteId(restaurante.getId())
                .stream()
                .anyMatch(pedido -> pedido.getCliente().getId().equals(cliente.getId()));

        if (!tienePedidos) {
            throw new BusinessException("No puedes reseñar un restaurante sin haber hecho un pedido");
        }

        Resenia resenia = new Resenia();
        resenia.setDescripcion(reseniaCreateDTO.getResenia());
        resenia.setRestaurante(restaurante);
        resenia.setPuntuacion(reseniaCreateDTO.getPuntuacion());
        resenia.setCliente(cliente);

        Resenia retorno = reseniaRepository.save(resenia);
        return new ReseniaDetailDTO(retorno.getId(), retorno.getCliente().getId(), retorno.getRestaurante().getId(), retorno.getDescripcion(), retorno.getPuntuacion());
    }

    public List<ReseniaResumenDTO> verReseniasRestaurante(String usuario){

        Restaurante restaurante = restauranteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("No existe ningún restaurante con ese nombre"));

        List<ReseniaResumenDTO> resenias = reseniaRepository.findByRestauranteId(restaurante.getId()).stream()
                .sorted(Comparator.comparingDouble(Resenia::getPuntuacion).reversed()
                        .thenComparing(resenia -> resenia.getCliente().getId()))
                .map(resenia -> new ReseniaResumenDTO
                        (resenia.getCliente().getId(),
                                resenia.getCliente().getNombreYapellido(),
                                resenia.getDescripcion(),
                                resenia.getPuntuacion())).toList();

        reseniaValidations.validarResenia(resenias);

        return resenias;
    }

    public void eliminarResenia(Long idResenia)
    {
        Resenia resenia = reseniaRepository.findById(idResenia).orElseThrow(()-> new RuntimeException("No existe esa reseña"));

        reseniaRepository.delete(resenia);
    }

    public List<ReseniaResumenDTO>verResenias(Long idRestaurante)
    {
        Restaurante restaurante = restauranteRepository.findById(idRestaurante).orElseThrow(()-> new RuntimeException("No existe ese restaurante"));

        List<ReseniaResumenDTO> resenias = new ArrayList<>();

        for(Resenia r : restaurante.getReseniasRestaurante()){
            ReseniaResumenDTO resenia = new ReseniaResumenDTO(
                    r.getCliente().getId(),
                    r.getCliente().getNombreYapellido(),
                    r.getDescripcion(),
                    r.getPuntuacion()
            );
            resenias.add(resenia);
        }

        if(resenias.isEmpty())
        {
            throw new RuntimeException("No hay resenias en este restaurante");
        }

        return resenias;
    }

    public List<ReseniaDetailDTO>verReseniasAdmin(Long idRestaurante)
    {
        Restaurante restaurante = restauranteRepository.findById(idRestaurante).orElseThrow(()-> new RuntimeException("No existe ese restaurante"));

        List<ReseniaDetailDTO> resenias = new ArrayList<>();

        for(Resenia r : restaurante.getReseniasRestaurante()){
            ReseniaDetailDTO resenia = new ReseniaDetailDTO(r.getId(),r.getCliente().getId(),r.getRestaurante().getId(), r.getDescripcion(), r.getPuntuacion());
            resenias.add(resenia);
        }

        if(resenias.isEmpty())
        {
            throw new RuntimeException("No hay resenias en este restaurante");
        }

        return resenias;
    }
}