package com.tecsup.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class ConfigController {
    @Value("${empresa.ambiente}")
    private String ambiente;
    @Value("${spring.application.name}")
    private String servicio;
    @Value("${server.port}")
    private String puerto;

    @GetMapping("/api/configuracion")
    public Map<String, String> configuracion() {
        return Map.of("ambiente", ambiente, "servicio", servicio, "puerto", puerto);
    }
}
