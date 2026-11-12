package com.tecsup.client;

import com.tecsup.dto.ClienteRemoto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cliente-service")
public interface ClienteClient {
    @GetMapping("/api/clientes/{id}")
    ClienteRemoto obtener(@PathVariable("id") Long id);
}
