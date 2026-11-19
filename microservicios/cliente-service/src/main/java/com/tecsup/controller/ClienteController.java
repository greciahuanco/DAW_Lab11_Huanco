package com.tecsup.controller;

import com.tecsup.dto.ClienteDTO;
import com.tecsup.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteService service;
    private final com.tecsup.services.PedidoCircuitService pedidos;
    private final com.tecsup.config.DemoraSimulada demora;

    public ClienteController(ClienteService service, com.tecsup.config.DemoraSimulada demora, com.tecsup.services.PedidoCircuitService pedidos) {
        this.service = service;
        this.demora = demora;
        this.pedidos = pedidos;
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}/pedidos")
    public ResponseEntity<?> pedidos(@PathVariable Long id) {
        service.obtener(id);
        return ResponseEntity.ok(pedidos.obtener(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        demora.esperar();
        return ResponseEntity.ok(service.obtener(id));
    }

    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody ClienteDTO dto) {
        return ResponseEntity.status(201).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok("Eliminado correctamente");
    }
}
