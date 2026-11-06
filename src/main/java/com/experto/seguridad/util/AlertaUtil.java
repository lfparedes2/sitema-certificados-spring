package com.experto.seguridad.util;

import com.experto.seguridad.dto.AlertaDto;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class AlertaUtil {

    private static final String ATTRIBUTE_NAME = "alertaNotificacion";
    private static final String DEFAULT_ERROR_COLOR = "#EF4444";
    private static final String DEFAULT_SUCCESS_COLOR = "#10B981";

    // --- MÉTODOS PARA MODEL (Vistas directas / Errores de validación) ---

    public static void error(Model model, String titulo, String mensaje, Integer tiempo, String colorBoton) {
        model.addAttribute(ATTRIBUTE_NAME, crearAlerta("error", titulo, mensaje, tiempo, colorBoton, DEFAULT_ERROR_COLOR));
    }

    public static void exito(Model model, String titulo, String mensaje) {
        model.addAttribute(ATTRIBUTE_NAME, crearAlerta("success", titulo, mensaje, 3000, null, DEFAULT_SUCCESS_COLOR));
    }

    // --- MÉTODOS PARA REDIRECT (Para usar después de un return "redirect:/...") ---

    public static void errorRedirect(RedirectAttributes attributes, String titulo, String mensaje, Integer tiempo, String colorBoton) {
        attributes.addFlashAttribute(ATTRIBUTE_NAME, crearAlerta("error", titulo, mensaje, tiempo, colorBoton, DEFAULT_ERROR_COLOR));
    }

    public static void exitoRedirect(RedirectAttributes attributes, String titulo, String mensaje) {
        attributes.addFlashAttribute(ATTRIBUTE_NAME, crearAlerta("success", titulo, mensaje, 3000, null, DEFAULT_SUCCESS_COLOR));
    }

    // Método privado interno para evitar repetir código al construir el DTO
    private static AlertaDto crearAlerta(String tipo, String titulo, String mensaje, Integer tiempo, String colorBoton, String colorDefecto) {
        return AlertaDto.builder()
                .tipo(tipo)
                .titulo(titulo)
                .mensaje(mensaje)
                .tiempo(tiempo)
                .colorBoton(colorBoton != null ? colorBoton : colorDefecto)
                .build();
    }
}