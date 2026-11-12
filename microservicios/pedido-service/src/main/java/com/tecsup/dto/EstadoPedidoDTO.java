package com.tecsup.dto;

import com.tecsup.model.EstadoPedido;
import jakarta.validation.constraints.NotNull;

public record EstadoPedidoDTO(@NotNull(message = "El estado es obligatorio") EstadoPedido estado) {
}
