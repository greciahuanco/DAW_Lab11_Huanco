package com.tecsup.dto;

import com.tecsup.model.DetallePedido;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PedidoResponse(Long idPedido, LocalDate fecha, Long idCliente,
                             BigDecimal total, List<DetallePedido> detalles) {
}
