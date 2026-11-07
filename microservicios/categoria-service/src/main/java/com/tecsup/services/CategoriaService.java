package com.tecsup.services;

import com.tecsup.dto.CategoriaDTO;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Categoria;
import com.tecsup.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository repo;

    public CategoriaService(CategoriaRepository repo) {
        this.repo = repo;
    }

    public List<Categoria> listar() {
        return repo.findAll();
    }

    public Categoria obtener(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Categoria no encontrada con id: " + id));
    }

    @Transactional
    public Categoria crear(CategoriaDTO dto) {
        return guardar(new Categoria(), dto);
    }

    @Transactional
    public Categoria actualizar(Long id, CategoriaDTO dto) {
        return guardar(obtener(id), dto);
    }

    private Categoria guardar(Categoria existente, CategoriaDTO dto) {
        existente.setNombre(dto.getNombre());
        existente.setEstado(dto.getEstado());
        return repo.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        repo.delete(obtener(id));
    }
}
