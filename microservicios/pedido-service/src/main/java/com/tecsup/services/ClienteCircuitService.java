package com.tecsup.services;

import com.tecsup.client.ClienteClient;
import com.tecsup.dto.ClienteRemoto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ClienteCircuitService {
    private static final Logger log = LoggerFactory.getLogger(ClienteCircuitService.class);
    private final ClienteClient client;

    public ClienteCircuitService(ClienteClient client) {
        this.client = client;
    }

    @CircuitBreaker(name = "clienteService", fallbackMethod = "fallback")
    @Retry(name = "clienteRetry")
    public ClienteRemoto obtener(Long id) {
        log.info("Llamando a cliente-service, ID {}", id);
        return client.obtener(id);
    }

    public ClienteRemoto fallback(Long id, feign.FeignException.NotFound ex) {
        throw ex;
    }

    public ClienteRemoto fallback(Long id, Exception ex) {
        log.warn("Fallback de cliente-service, ID {}, causa {}", id, ex.getClass().getSimpleName());
        return new ClienteRemoto(id, "Cliente temporal no disponible", null);
    }
}
