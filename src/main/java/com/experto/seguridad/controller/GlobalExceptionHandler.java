package com.experto.seguridad.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.security.access.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(AccessDeniedException ex, Model model) {
        model.addAttribute("status", "403");
        model.addAttribute("title", "Acceso Denegado");
        model.addAttribute("message", "No tienes los permisos asignados en tu rol para visualizar este recurso.");
        return "error";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundError(NoHandlerFoundException ex, Model model) {
        model.addAttribute("status", "404");
        model.addAttribute("title", "Pagina No Encontrada");
        model.addAttribute("message", "El recurso dinamico solicitado no existe en el esquema de la aplicacion.");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericError(Exception ex, Model model) {
        model.addAttribute("status", "500");
        model.addAttribute("title", "Error Interno del Servidor");
        model.addAttribute("message", "Ocurrio una anomalia inesperada. El equipo de infraestructura ha sido notificado.");
        return "error";
    }
}
