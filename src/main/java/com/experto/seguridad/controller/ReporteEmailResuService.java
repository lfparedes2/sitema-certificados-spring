package com.experto.seguridad.controller;

import com.experto.seguridad.dto.EmailRequest;
import com.experto.seguridad.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReporteEmailResuService {

    private final EmailService emailService;

    public void procesarRecepcionReporte(String correoOficial, String nombreOficial, String institucion) {
        // 1. Lógica de negocio (guardar reporte, validar datos, etc.)
        String fechaFormateada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        // 2. Armar los parámetros exactos que espera la plantilla #{clave}
        Map<String, String> parametros = Map.of(
                "nombre.usuario", nombreOficial,
                "nombre.institucion", institucion,
                "fecha.ingreso", fechaFormateada
        );

        // 3. Construir la solicitud y disparar el envío asíncrono
        EmailRequest request = EmailRequest.builder()
                .codigoEvento("ACUSE_RECIBO_RESU") // Debe coincidir con la columna 'codigo' en BD
                .destinatario(correoOficial)
                .parametros(parametros)
                .build();

        emailService.enviarCorreoEvento(request);
    }
}
