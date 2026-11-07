package com.tecsup.dto;

import jakarta.validation.constraints.*;
import java.time.*;

public record CitaDTO(
        @NotNull LocalDate fecha,
        @NotNull LocalTime hora,
        @NotNull @Positive Integer idPaciente,
        @NotNull @Positive Integer idMedico,
        @Size(max=300) String motivo
) {
}
