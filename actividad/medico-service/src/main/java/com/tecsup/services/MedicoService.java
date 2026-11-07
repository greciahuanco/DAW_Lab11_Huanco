package com.tecsup.services;

import com.tecsup.dto.MedicoDTO;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Medico;
import com.tecsup.repository.MedicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class MedicoService {
    private final MedicoRepository repo;
    private final org.springframework.jdbc.core.JdbcTemplate jdbc;

    public MedicoService(MedicoRepository repo, org.springframework.jdbc.core.JdbcTemplate jdbc) {
        this.repo = repo;
        this.jdbc = jdbc;
    }

    public List<Medico> listar() {
        return repo.findAll();
    }

    public Medico obtener(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Medico no encontrado con ID " + id));
    }

    @Transactional
    public Medico crear(MedicoDTO dto) {
        return guardar(new Medico(), dto);
    }

    @Transactional
    public Medico actualizar(Integer id, MedicoDTO dto) {
        return guardar(obtener(id), dto);
    }

    private Medico guardar(Medico entity, MedicoDTO dto) {
        if (!Boolean.TRUE.equals(jdbc.queryForObject(
                "SELECT EXISTS(SELECT 1 FROM especialidad WHERE id_especialidad = ?)",
                Boolean.class, dto.idEspecialidad()))) {
            throw new IllegalArgumentException("La especialidad no existe");
        }
        entity.setCmp(dto.cmp());
        entity.setNombres(dto.nombres());
        entity.setApellidos(dto.apellidos());
        entity.setTelefono(dto.telefono());
        entity.setCorreo(dto.correo());
        entity.setIdEspecialidad(dto.idEspecialidad());
        entity.setEstado(dto.estado() == null ? "A" : dto.estado());
        return repo.save(entity);
    }

    @Transactional
    public void eliminar(Integer id) {
        repo.delete(obtener(id));
    }
}
