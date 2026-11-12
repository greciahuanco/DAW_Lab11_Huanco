package com.tecsup.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(assignableTypes = CategoriaController.class)
public class InstanciaController {
    @Value("${server.port}")
    private int puerto;

    @ModelAttribute
    public void identificar(HttpServletResponse response) {
        response.setHeader("X-Instancia", Integer.toString(puerto));
    }
}
