package com.experto.seguridad.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;


public record UsuarioRegistroDto(
        @Size(min = 4, max = 50) String username,
        @NotBlank(message = "La cedula es obligatoria") @Size(max = 15) String cedula,
        @Size(min = 8, max = 300) String contrasenia,
        @NotBlank(message = "El email es obligatorio") @Email @Size(max = 50) String email,
        @NotBlank(message = "El nombre completo es obligatorio") @Size(max = 100) String nombre,
        @NotBlank(message = "El tipo de identificación es obligatorio") @Size(max = 100) String tipoIdentificacion,
        String tipo,

        @Pattern(regexp = "\\d{4}", message = "El código debe tener exactamente 4 dígitos")
        String codeVerifica,

        @Future(message = "La fecha de caducidad del pasaporte debe ser futura")
        LocalDate fechaCaducidaPasaporte // Opcional: solo se envía si es pasaporte


) {
}
