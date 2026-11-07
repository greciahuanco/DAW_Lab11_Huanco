package com.tecsup.services;

import com.tecsup.dto.PacienteDTO;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Paciente;
import com.tecsup.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PacienteService {
    private final PacienteRepository repo;

    public PacienteService(PacienteRepository repo) {
        this.repo = repo;

    }

    public List<Paciente> listar() {
        return repo.findAll();
    }

    public Paciente obtener(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con ID " + id));
    }

    @Transactional
    public Paciente crear(PacienteDTO dto) {
        return guardar(new Paciente(), dto);
    }

    @Transactional
    public Paciente actualizar(Integer id, PacienteDTO dto) {
        return guardar(obtener(id), dto);
    }

    private Paciente guardar(Paciente entity, PacienteDTO dto) {
        entity.setDni(dto.dni());
        entity.setNombres(dto.nombres());
        entity.setApellidos(dto.apellidos());
        entity.setFechaNacimiento(dto.fechaNacimiento());
        entity.setSexo(dto.sexo());
        entity.setTelefono(dto.telefono());
        entity.setDireccion(dto.direccion());
        entity.setCorreo(dto.correo());
        entity.setEstado(dto.estado() == null ? "A" : dto.estado());
        return repo.save(entity);
    }

    @Transactional
    public void eliminar(Integer id) {
        repo.delete(obtener(id));
    }
}
