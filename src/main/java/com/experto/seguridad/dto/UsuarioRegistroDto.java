package com.experto.seguridad.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;


public record UsuarioRegistroDto(
    @NotBlank(message = "El username es obligatorio") @Size(min = 4, max = 50) String username,
    @NotBlank(message = "La cedula es obligatoria") @Size(max = 15) String cedula,
    @NotBlank(message = "La contrasenia es obligatoria") @Size(min = 8, max = 300) String contrasenia,
    @NotBlank(message = "El email es obligatorio") @Email @Size(max = 50) String email,
    @NotBlank(message = "El nombre completo es obligatorio") @Size(max = 100) String nombre,
    @NotBlank(message = "El tipo de usuario es obligatorio") String tipo,

    @NotNull(message = "La fecha de creación es obligatoria")
    @PastOrPresent(message = "La fecha de creación no puede ser futura")
    LocalDate fechaCreacion,

    @NotNull(message = "La fecha de caducidad del password es obligatoria")
    @Future(message = "La fecha de caducidad del password debe ser una fecha futura")
    ZonedDateTime fechaCaducaPasword,

    @NotNull(message = "El código de verificación es obligatorio")
    @Min(value = 100000, message = "El código debe tener 4 dígitos")
    @Max(value = 999999, message = "El código no puede tener más de 4 dígitos")
    Integer codeVerifica

) {}
