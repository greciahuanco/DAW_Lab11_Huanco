package com.tecsup.dto;

import java.util.List;

public record ClientePedidosResponse(Long clienteId, boolean disponible,
                                    List<PedidoResumen> pedidos, String mensaje) {
}
