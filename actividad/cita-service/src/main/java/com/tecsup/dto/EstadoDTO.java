package com.tecsup.dto;

import jakarta.validation.constraints.NotBlank;

public record EstadoDTO(@NotBlank String estado) {
}
