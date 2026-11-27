package com.experto.seguridad.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;


public record UsuarioRegistroDto(
        @Size(min = 4, max = 50, message = "El usuario debe tener entre 4 y 50 caracteres")
        String username,

        @NotBlank(message = "La cédula o identificación es obligatoria")
        @Size(max = 15, message = "Máximo 15 caracteres")
        String cedula,

        @Size(min = 8, max = 300, message = "La contraseña debe tener al menos 8 caracteres")
        String contrasenia,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Formato de correo inválido")
        @Size(max = 50)
        String email,

        @NotBlank(message = "Debe confirmar su correo electrónico")
        String repitaEmail,

        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(max = 100)
        String nombre,

        @NotBlank(message = "El tipo de identificación es obligatorio")
        @Size(max = 100)
        String tipoIdentificacion,

        String tipo,

        @Pattern(regexp = "^$|^\\d{4}$", message = "El código debe tener exactamente 4 dígitos")
        String codeVerifica,

        @Future(message = "La fecha de caducidad del pasaporte debe ser futura")
        LocalDate fechaCaducidaPasaporte
) {
    public static UsuarioRegistroDto vacio() {
        return new UsuarioRegistroDto(null, null, null, null, null, null, "C", "EXTERNO", null, null);
    }

    @AssertTrue(message = "Los correos electrónicos no coinciden.")
    public boolean isEmailCoincidente() {
        if (email == null || repitaEmail == null) {
            return true;
        }
        return email.equalsIgnoreCase(repitaEmail);
    }
}
