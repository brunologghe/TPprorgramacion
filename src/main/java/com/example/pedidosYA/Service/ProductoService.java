package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.ProductoDTO.ProductoCrearDTO;
import com.example.pedidosYA.DTO.ProductoDTO.ProductoDetailDTO;
import com.example.pedidosYA.DTO.ProductoDTO.ProductoModificarDTO;
import com.example.pedidosYA.DTO.ProductoDTO.ProductoResumenDTO;
import com.example.pedidosYA.DTO.RestauranteDTO.RestauranteResumenDTO;
import com.example.pedidosYA.Exceptions.BusinessException;
import com.example.pedidosYA.Model.Producto;
import com.example.pedidosYA.Model.Restaurante;
import com.example.pedidosYA.Repository.ProductoRepository;
import com.example.pedidosYA.Repository.RestauranteRepository;
import com.example.pedidosYA.Security.AuthUtil;
import com.example.pedidosYA.Validations.ProductoValidations;
import com.example.pedidosYA.Validations.RestauranteValidations;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoValidations productoValidations;

    @Autowired
    private RestauranteRepository restauranteRepository;

    public Set<ProductoResumenDTO> findAllProductosByRestaurante(String usuario){

        Restaurante restaurante = restauranteRepository.findByUsuario(usuario).orElseThrow(() -> new BusinessException("No existe restaurante con ese nombre"));
        Set<Producto> lista = restaurante.getMenu();
        productoValidations.validarListaVacia(lista);
        return lista.stream().map(p -> new ProductoResumenDTO(p.getId(), p.getNombre(), p.getPrecio())).collect(Collectors.toSet());
    }

    public ProductoDetailDTO findProductoBynombre (String usuario, String nombre){
        Restaurante rest = restauranteRepository.findByUsuario(usuario).orElseThrow(() -> new BusinessException("No existe restaurante con ese nombre"));

        productoValidations.validarNombreProductoExistente(nombre);
        Producto p =productoRepository.findByNombreAndRestauranteId(nombre, rest.getId());

        RestauranteResumenDTO restResumen = new RestauranteResumenDTO(rest.getId(), rest.getNombre());
        return new ProductoDetailDTO(p.getId(), p.getNombre(), p.getCaracteristicas(), p.getPrecio(), p.getStock(), restResumen);
    }

    @Transactional
    public ProductoDetailDTO crearProducto(String usuario, ProductoCrearDTO productoDTO){

        Restaurante rest = restauranteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("No existe restaurante con ese nombre"));

        productoValidations.validarProductoContieneRestaurante(rest.getMenu(), productoDTO.getNombre());

        Producto p = new Producto();
        p.setNombre(productoDTO.getNombre());
        p.setCaracteristicas(productoDTO.getCaracteristicas());
        p.setPrecio(productoDTO.getPrecio());
        p.setStock(productoDTO.getStock());
        p.setRestaurante(rest);

        rest.getMenu().add(p);
        restauranteRepository.save(rest);

        Producto uProd = rest.getMenu().stream()
                .filter(prod -> prod.getNombre().equals(productoDTO.getNombre()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("No se encontró el producto recién agregado"));

        RestauranteResumenDTO restauranteResumenDTO = new RestauranteResumenDTO(rest.getId(), rest.getNombre());

        return new ProductoDetailDTO(uProd.getId(), p.getNombre(), p.getCaracteristicas(), p.getPrecio(), p.getStock(), restauranteResumenDTO);
    }

    @Transactional
    public ProductoDetailDTO modificarProducto(String usuario, Long idProducto, ProductoModificarDTO productoNuevo) {

        Restaurante restaurante = restauranteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("No existe ningún restaurante con ese nombre"));

        Producto producto = restaurante.getMenu().stream()
                .filter(p -> p.getId().equals(idProducto))
                .findFirst()
                .orElseThrow(() -> new BusinessException("No existe ningún producto con ese id en este restaurante"));

        if (!producto.getRestaurante().getUsuario().equals(usuario)) {
            throw new BusinessException("El producto no pertenece al restaurante elegido");
        }

        if (productoNuevo.precio() < 0 || productoNuevo.precio() > 400000) {
            throw new BusinessException("El precio no puede ser negativo.");
        }
        if (productoNuevo.stock() < 0) {
            throw new BusinessException("El stock no puede ser negativo.");
        }
        if (productoNuevo.nombre() == null || productoNuevo.nombre().isBlank()) {
            throw new BusinessException("El nombre del producto no puede estar vacío.");
        }

        productoValidations.validarProductoContieneRestaurantePorNombre(restaurante.getMenu(), producto);

        restaurante.getMenu().remove(producto);

        producto.setNombre(productoNuevo.nombre());
        producto.setCaracteristicas(productoNuevo.caracteristicas());
        producto.setPrecio(productoNuevo.precio());
        producto.setStock(productoNuevo.stock());

        restaurante.getMenu().add(producto);

        restauranteRepository.save(restaurante);

        RestauranteResumenDTO restauranteDTO = new RestauranteResumenDTO(
                restaurante.getId(), restaurante.getNombre()
        );

        return new ProductoDetailDTO(
                producto.getId(), producto.getNombre(), producto.getCaracteristicas(),
                producto.getPrecio(), producto.getStock(), restauranteDTO
        );
    }

    @Transactional
    public void eliminarProducto (String usuario, Long idProducto){

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new BusinessException("NO existe ningún producto con ese id"));

        Restaurante restaurante = restauranteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessException("No existe ningún restaurante con ese nombre"));

        productoValidations.validarProductoPerteneceARestaurante(restaurante.getId(), producto);
        productoValidations.validarProductoEnRestaurante(restaurante, producto);
        productoValidations.validarProductoNoAsociadoAPedidos(producto, restaurante);

        restaurante.getMenu().remove(producto);
        restauranteRepository.save(restaurante);
    }
}