package com.tecsup.dto;

import com.tecsup.model.EstadoPedido;
import java.math.BigDecimal;

public record PedidoResumen(Long idPedido, BigDecimal total, EstadoPedido estado) {
}
