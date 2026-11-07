package com.tecsup.services;

import com.tecsup.dto.CitaDTO;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Cita;
import com.tecsup.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class CitaService {
    private final CitaRepository repo;
    private final RestClient cliente;
    private final String pacientes;
    private final String medicos;

    public CitaService(CitaRepository repo, @Value("${servicios.pacientes}") String pacientes,
                       @Value("${servicios.medicos}") String medicos) {
        this.repo = repo;
        this.pacientes = pacientes;
        this.medicos = medicos;
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(3000);
        this.cliente = RestClient.builder().requestFactory(factory).build();
    }

    public List<Cita> listar() {
        return repo.findAll();
    }

    public Cita obtener(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID " + id));
    }

    public Cita crear(CitaDTO dto) {
        verificar(pacientes, dto.idPaciente(), "paciente");
        verificar(medicos, dto.idMedico(), "médico");
        Cita cita = new Cita();
        cita.setFecha(dto.fecha());
        cita.setHora(dto.hora());
        cita.setIdPaciente(dto.idPaciente());
        cita.setIdMedico(dto.idMedico());
        cita.setMotivo(dto.motivo());
        cita.setEstado("PROGRAMADA");
        return repo.save(cita);
    }

    private void verificar(String url, Integer id, String recurso) {
        try {
            cliente.get().uri(url + "/{id}", id).retrieve().toBodilessEntity();
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new ResourceNotFoundException("No existe el " + recurso + " con ID " + id);
            }
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo consultar el servicio de " + recurso);
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo consultar el servicio de " + recurso);
        }
    }

    @Transactional
    public Cita actualizarEstado(Integer id, String estado) {
        Cita cita = obtener(id);
        String actual = cita.getEstado();
        if (actual.equals(estado)) {
            return cita;
        }
        boolean permitido = switch (actual) {
            case "PROGRAMADA" -> estado.equals("CONFIRMADA") || estado.equals("CANCELADA");
            case "CONFIRMADA" -> estado.equals("ATENDIDA") || estado.equals("CANCELADA");
            default -> false;
        };
        if (!permitido) {
            throw new IllegalArgumentException("El cambio de estado no está permitido");
        }
        cita.setEstado(estado);
        return repo.save(cita);
    }
}
