package com.tecsup.services;

import com.tecsup.client.CategoriaClient;
import com.tecsup.dto.CategoriaRemoto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CategoriaCircuitService {
    private static final Logger log = LoggerFactory.getLogger(CategoriaCircuitService.class);
    private final CategoriaClient client;

    public CategoriaCircuitService(CategoriaClient client) {
        this.client = client;
    }

    @Bulkhead(name = "categoriaBulkhead", fallbackMethod = "bulkheadFallback")
    @CircuitBreaker(name = "categoriaService", fallbackMethod = "fallback")
    @Retry(name = "categoriaRetry")
    public CategoriaRemoto obtener(Long id) {
        log.info("Llamando a categoria-service, ID {}", id);
        return client.obtener(id);
    }

    public CategoriaRemoto bulkheadFallback(Long id, io.github.resilience4j.bulkhead.BulkheadFullException ex) {
        return fallback(id, (Exception) ex);
    }

    public CategoriaRemoto fallback(Long id, feign.FeignException.NotFound ex) {
        throw ex;
    }

    public CategoriaRemoto fallback(Long id, Exception ex) {
        log.warn("Fallback de categoria-service, ID {}, causa {}", id, ex.getClass().getSimpleName());
        return new CategoriaRemoto(id, "Categoría temporal no disponible", null);
    }
}
