package com.tecsup.client;

import com.tecsup.dto.ProductoRemoto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "producto-service")
public interface ProductoClient {
    @GetMapping("/api/productos/{id}")
    ProductoRemoto obtener(@PathVariable("id") Long id);
}
