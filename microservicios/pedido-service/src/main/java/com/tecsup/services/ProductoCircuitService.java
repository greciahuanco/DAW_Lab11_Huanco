package com.tecsup.services;

import com.tecsup.client.ProductoClient;
import com.tecsup.dto.ProductoRemoto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductoCircuitService {
    private static final Logger log = LoggerFactory.getLogger(ProductoCircuitService.class);
    private final ProductoClient client;

    public ProductoCircuitService(ProductoClient client) {
        this.client = client;
    }

    @Bulkhead(name = "productoBulkhead", fallbackMethod = "bulkheadFallback")
    @CircuitBreaker(name = "productoService", fallbackMethod = "fallback")
    public ProductoRemoto obtener(Long id) {
        log.info("Llamando a producto-service, ID {}", id);
        return client.obtener(id);
    }

    public ProductoRemoto bulkheadFallback(Long id, io.github.resilience4j.bulkhead.BulkheadFullException ex) {
        return fallback(id, (Exception) ex);
    }

    public ProductoRemoto fallback(Long id, feign.FeignException.NotFound ex) {
        throw ex;
    }

    public ProductoRemoto fallback(Long id, Exception ex) {
        log.warn("Fallback de producto-service, ID {}, causa {}", id, ex.getClass().getSimpleName());
        return new ProductoRemoto(id, "Producto temporal no disponible", null);
    }
}
