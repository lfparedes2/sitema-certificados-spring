package com.experto.seguridad.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class EmailRequest {

    @NotBlank(message = "El código del evento es obligatorio")
    private final String codigoEvento;

    @NotBlank(message = "El destinatario es obligatorio")
    @Email(message = "Formato de correo destinatario inválido")
    private final String destinatario;

    private final List<@Email String> copias; // CC opcional

    @Builder.Default
    private final Map<String, String> parametros = Collections.emptyMap();
}
