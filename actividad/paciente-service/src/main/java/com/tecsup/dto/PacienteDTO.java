package com.tecsup.dto;

import jakarta.validation.constraints.*;
import java.time.*;

public record PacienteDTO(
        @Pattern(regexp="[0-9]{8}") String dni,
        @NotBlank @Size(max=100) String nombres,
        @NotBlank @Size(max=100) String apellidos,
        @Past LocalDate fechaNacimiento,
        @Size(max=1) String sexo,
        @Size(max=15) String telefono,
        @Size(max=200) String direccion,
        @Email @Size(max=100) String correo,
        @Pattern(regexp="[AI]") String estado
) {
}
