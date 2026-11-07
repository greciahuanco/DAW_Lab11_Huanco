package com.tecsup.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ClienteDTO {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre admite hasta 100 caracteres")
    private String nombre;
    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos admiten hasta 100 caracteres")
    private String apellidos;
    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200, message = "La dirección admite hasta 200 caracteres")
    private String direccion;
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "9[0-9]{8}", message = "El teléfono debe tener 9 dígitos y comenzar con 9")
    private String telefono;
    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "[0-9]{8}", message = "El DNI debe tener 8 dígitos")
    private String nroDni;
    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "ACTIVO|INACTIVO", message = "El estado debe ser ACTIVO o INACTIVO")
    private String estado;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNroDni() {
        return nroDni;
    }

    public void setNroDni(String nroDni) {
        this.nroDni = nroDni;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
