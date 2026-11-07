package com.tecsup.services;

import com.tecsup.dto.ClienteDTO;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Cliente;
import com.tecsup.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ClienteService {
    private final ClienteRepository repo;

    public ClienteService(ClienteRepository repo) {
        this.repo = repo;
    }

    public List<Cliente> listar() {
        return repo.findAll();
    }

    public Cliente obtener(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Cliente no encontrado con id: " + id));
    }

    @Transactional
    public Cliente crear(ClienteDTO dto) {
        return guardar(new Cliente(), dto);
    }

    @Transactional
    public Cliente actualizar(Long id, ClienteDTO dto) {
        return guardar(obtener(id), dto);
    }

    private Cliente guardar(Cliente existente, ClienteDTO dto) {
        existente.setNombre(dto.getNombre());
        existente.setApellidos(dto.getApellidos());
        existente.setDireccion(dto.getDireccion());
        existente.setTelefono(dto.getTelefono());
        existente.setNroDni(dto.getNroDni());
        existente.setEstado(dto.getEstado());
        return repo.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        repo.delete(obtener(id));
    }
}
