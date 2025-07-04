package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaCreateDTO;
import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaDetailDTO;
import com.example.pedidosYA.DTO.ReseniaDTO.ReseniaResumenDTO;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.Cliente;
import com.example.pedidosYA.Model.Resenia;
import com.example.pedidosYA.Model.Restaurante;
import com.example.pedidosYA.Repository.ClienteRepository;
import com.example.pedidosYA.Repository.ReseniaRepository;
import com.example.pedidosYA.Repository.RestauranteRepository;
import com.example.pedidosYA.Validations.ClienteValidations;
import com.example.pedidosYA.Validations.ReseniaValidations;
import com.example.pedidosYA.Validations.RestauranteValidations;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Transactional
    public ReseniaDetailDTO crearResenia(String usuario, ReseniaCreateDTO reseniaCreateDTO) {
        Cliente cliente = clienteRepository.findByUsuario(usuario).orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        Restaurante restaurante = restauranteValidations.validarExisteId(reseniaCreateDTO.getRestauranteId());

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
                        (resenia.getCliente().getId(), resenia.getDescripcion(), resenia.getPuntuacion())).toList();

        reseniaValidations.validarResenia(resenias);

        return resenias;
    }
}
