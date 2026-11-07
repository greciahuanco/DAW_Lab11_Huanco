package com.tecsup.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PedidoDTO {
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
    @NotNull(message = "El cliente es obligatorio")
    @Positive(message = "El ID de cliente debe ser positivo")
    private Long idCliente;
    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.00", message = "El total no puede ser negativo")
    @Digits(integer = 10, fraction = 2, message = "El total admite hasta 10 enteros y 2 decimales")
    private BigDecimal total;
    @NotEmpty(message = "El pedido debe tener al menos un detalle")
    @Size(max = 100, message = "El pedido admite hasta 100 detalles")
    @Valid
    private List<@NotNull(message = "El detalle no puede ser nulo") DetallePedidoDTO> detalles;

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<@NotNull(message = "El detalle no puede ser nulo") DetallePedidoDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<@NotNull(message = "El detalle no puede ser nulo") DetallePedidoDTO> detalles) {
        this.detalles = detalles;
    }
}
