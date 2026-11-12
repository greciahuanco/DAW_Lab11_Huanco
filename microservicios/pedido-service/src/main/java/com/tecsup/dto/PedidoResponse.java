package com.tecsup.dto;

import com.tecsup.model.EstadoPedido;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PedidoResponse(Long id, Long idPedido, LocalDate fecha, Long idCliente, BigDecimal total,
                             EstadoPedido estado, ClienteRemoto cliente, Integer cantidad,
                             ProductoRemoto producto, List<Detalle> detalles) {
    public record Detalle(Long idDetalle, Long idPedido, Long idProducto, Integer cantidad,
                          BigDecimal precio, ProductoRemoto producto) {
    }
}
