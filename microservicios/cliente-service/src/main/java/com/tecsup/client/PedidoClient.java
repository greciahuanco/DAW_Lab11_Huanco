package com.tecsup.client;

import com.tecsup.dto.PedidoResumen;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "pedido-service")
public interface PedidoClient {
    @GetMapping("/api/pedidos/cliente/{id}")
    List<PedidoResumen> obtener(@PathVariable("id") Long id);
}
