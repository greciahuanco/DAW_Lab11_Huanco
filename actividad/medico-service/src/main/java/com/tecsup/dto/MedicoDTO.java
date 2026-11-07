package com.tecsup.dto;

import jakarta.validation.constraints.*;
import java.time.*;

public record MedicoDTO(
        @Size(max=20) String cmp,
        @NotBlank @Size(max=100) String nombres,
        @NotBlank @Size(max=100) String apellidos,
        @Size(max=15) String telefono,
        @Email @Size(max=100) String correo,
        @NotNull @Positive Integer idEspecialidad,
        @Pattern(regexp="[AI]") String estado
) {
}
