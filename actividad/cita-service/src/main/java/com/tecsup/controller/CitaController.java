package com.tecsup.controller;

import com.tecsup.dto.*;
import com.tecsup.model.Cita;
import com.tecsup.services.CitaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {
    private final CitaService service;

    public CitaController(CitaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Cita> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Cita obtener(@PathVariable Integer id) {
        return service.obtener(id);
    }

    @PostMapping
    public ResponseEntity<Cita> crear(@Valid @RequestBody CitaDTO dto) {
        return ResponseEntity.status(201).body(service.crear(dto));
    }

    @PatchMapping("/{id}/estado")
    public Cita actualizarEstado(@PathVariable Integer id, @Valid @RequestBody EstadoDTO dto) {
        return service.actualizarEstado(id, dto.estado());
    }

    @PatchMapping("/{id}/cancelar")
    public Cita cancelar(@PathVariable Integer id) {
        return service.actualizarEstado(id, "CANCELADA");
    }

}
