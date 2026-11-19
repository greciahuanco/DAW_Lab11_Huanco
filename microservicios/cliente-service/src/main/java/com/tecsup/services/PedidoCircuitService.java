package com.tecsup.services;

import com.tecsup.client.PedidoClient;
import com.tecsup.dto.ClientePedidosResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PedidoCircuitService {
    private static final Logger log = LoggerFactory.getLogger(PedidoCircuitService.class);
    private final PedidoClient client;

    public PedidoCircuitService(PedidoClient client) {
        this.client = client;
    }

    @CircuitBreaker(name = "pedidoService", fallbackMethod = "fallback")
    public ClientePedidosResponse obtener(Long id) {
        log.info("Llamando a pedido-service, cliente {}", id);
        return new ClientePedidosResponse(id, true, client.obtener(id), "Pedidos consultados");
    }

    public ClientePedidosResponse fallback(Long id, Exception ex) {
        log.warn("Fallback de pedido-service, cliente {}, causa {}", id, ex.getClass().getSimpleName());
        return new ClientePedidosResponse(id, false, List.of(), "Pedidos temporalmente no disponibles");
    }
}
