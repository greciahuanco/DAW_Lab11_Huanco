package com.tecsup.dto;

import java.math.BigDecimal;

public record PedidoResumen(Long idPedido, BigDecimal total, String estado) {
}
