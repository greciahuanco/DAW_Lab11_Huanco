package com.tecsup.services;

import com.tecsup.dto.ProductoDTO;
import com.tecsup.client.CategoriaClient;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Producto;
import com.tecsup.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository repo;

    private final CategoriaClient categorias;

    public ProductoService(ProductoRepository repo, CategoriaClient categorias) {
        this.repo = repo;
        this.categorias = categorias;
    }

    public List<Producto> listar() {
        return repo.findAll().stream().map(this::completar).toList();
    }

    public Producto obtener(Long id) {
        return completar(buscar(id));
    }

    private Producto buscar(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    @Transactional
    public Producto crear(ProductoDTO dto) {
        return guardar(new Producto(), dto);
    }

    @Transactional
    public Producto actualizar(Long id, ProductoDTO dto) {
        return guardar(buscar(id), dto);
    }

    private Producto guardar(Producto existente, ProductoDTO dto) {
        existente.setNombre(dto.getNombre());
        existente.setPrecio(dto.getPrecio());
        existente.setStock(dto.getStock());
        existente.setCategoriaId(dto.getCategoriaId());
        return repo.save(completar(existente));
    }

    private Producto completar(Producto producto) {
        producto.setCategoria(categorias.obtener(producto.getCategoriaId()));
        return producto;
    }

    @Transactional
    public void eliminar(Long id) {
        repo.delete(buscar(id));
    }
}
