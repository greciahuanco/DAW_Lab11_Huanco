package com.tecsup.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PedidoRegistroDTO(
        @JsonAlias("idCliente") @NotNull(message = "El cliente es obligatorio") @Positive Long clienteId,
        @Positive Long productoId,
        @Positive(message = "La cantidad debe ser mayor que cero") Integer cantidad,
        LocalDate fecha,
        @PositiveOrZero @Digits(integer = 10, fraction = 2) BigDecimal total,
        @Size(min = 1, max = 100) List<@NotNull @Valid DetallePedidoDTO> detalles) {

    @AssertTrue(message = "Indique productoId y cantidad, o fecha, total y detalles")
    public boolean isFormatoValido() {
        if (detalles == null) {
            return productoId != null && cantidad != null && total == null;
        }
        return productoId == null && cantidad == null && fecha != null && total != null;
    }
}
