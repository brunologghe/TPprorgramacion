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
import com.example.pedidosYA.Validations.ProductoValidations;
import com.example.pedidosYA.Validations.RestauranteValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    ProductoValidations productoValidations;

    @Autowired
    RestauranteRepository restauranteRepository;

    @Autowired
    RestauranteValidations restauranteValidations;

    public Set<ProductoResumenDTO> findAllProductosByRestauranteId(Long id){

        Restaurante restaurante = restauranteRepository.findById(id).orElseThrow(() -> new BusinessException("No existe restaurante con ese id"));
        Set<Producto> lista = restaurante.getMenu();
        if (lista.isEmpty()) {
            throw new BusinessException("No hay productos cargados actualmente");
        }
        return lista.stream().map(p -> new ProductoResumenDTO(p.getId(), p.getNombre(), p.getPrecio())).collect(Collectors.toSet());
    }

    public ProductoDetailDTO findProductoBynombreAndIdRestaurante (Long idRestaurante, String nombre){

        productoValidations.validarNombreProductoExistente(nombre);
        Producto p =productoRepository.findByNombreAndRestauranteId(nombre, idRestaurante);

        Restaurante restaurante = restauranteRepository.findById(idRestaurante).orElseThrow(() -> new BusinessException("No existe restaurante con ese id"));
        RestauranteResumenDTO restResumen = new RestauranteResumenDTO(idRestaurante, restaurante.getNombre());
        return new ProductoDetailDTO(p.getId(), p.getNombre(), p.getCaracteristicas(), p.getPrecio(), p.getStock(), restResumen);
    }

    public ProductoDetailDTO crearProducto(ProductoCrearDTO producto){

        Producto pAux = productoRepository.findByNombre(producto.getNombre());
        if (pAux != null) {
            productoValidations.validarNombreProductoNoDuplicado(pAux.getId(), producto.getNombre());
        }
        if (producto.getPrecio() < 0) {
            throw new BusinessException("El precio no puede ser negativo.");
        }

        Producto p = new Producto();
        Restaurante rest = restauranteRepository.findById(producto.getRestaurante()).orElseThrow(() -> new BusinessException("No se encontro el id de restaurante"));

        p.setNombre(producto.getNombre());
        p.setCaracteristicas(producto.getCaracteristicas());
        p.setPrecio(producto.getPrecio());
        p.setStock(producto.getStock());
        p.setRestaurante(rest);

        rest.getMenu().add(p);

        restauranteRepository.save(rest);

        Producto uProd = rest.getMenu().stream()
                .filter(prod -> prod.getNombre().equals(producto.getNombre()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("No se encontró el producto recién agregado"));

        RestauranteResumenDTO restauranteResumenDTO = new RestauranteResumenDTO(rest.getId(), rest.getNombre());

        return new ProductoDetailDTO(uProd.getId(), p.getNombre(), p.getCaracteristicas(), p.getPrecio(), p.getStock(), restauranteResumenDTO);
    }


    public ProductoDetailDTO modificarProducto(Long idRestaurante, Long idProducto, ProductoModificarDTO productoNuevo) {

        Restaurante restaurante = restauranteRepository.findById(idRestaurante)
                .orElseThrow(() -> new BusinessException("No existe ningún restaurante con ese id"));

        Producto producto = restaurante.getMenu().stream()
                .filter(p -> p.getId().equals(idProducto))
                .findFirst()
                .orElseThrow(() -> new BusinessException("No existe ningún producto con ese id en este restaurante"));

        if (!producto.getRestaurante().getId().equals(idRestaurante)) {
            throw new BusinessException("El producto no pertenece al restaurante elegido");
        }

        if (productoNuevo.precio() < 0) {
            throw new BusinessException("El precio no puede ser negativo.");
        }
        if (productoNuevo.stock() < 0) {
            throw new BusinessException("El stock no puede ser negativo.");
        }
        if (productoNuevo.nombre() == null || productoNuevo.nombre().isBlank()) {
            throw new BusinessException("El nombre del producto no puede estar vacío.");
        }

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

    public void eliminarProducto (Long idRestaurante, Long idProducto){

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new BusinessException("NO existe ningún producto con ese id"));

        Restaurante restaurante = restauranteRepository.findById(idRestaurante)
                .orElseThrow(() -> new BusinessException("No existe ningún restaurante con ese id"));

        productoValidations.validarProductoPerteneceARestaurante(idRestaurante, producto);
        productoValidations.validarProductoEnRestaurante(restaurante, producto);

        restaurante.getMenu().remove(producto);

        restauranteRepository.save(restaurante);
    }
}