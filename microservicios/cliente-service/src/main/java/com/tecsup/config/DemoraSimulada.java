package com.tecsup.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DemoraSimulada {
    private final long milisegundos;

    public DemoraSimulada(@Value("${simulacion.demora-ms:0}") long milisegundos) {
        if (milisegundos < 0 || milisegundos > 10000) {
            throw new IllegalArgumentException("La demora debe estar entre 0 y 10000 ms");
        }
        this.milisegundos = milisegundos;
    }

    public void esperar() {
        if (milisegundos == 0) {
            return;
        }
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("La consulta fue interrumpida", ex);
        }
    }
}
