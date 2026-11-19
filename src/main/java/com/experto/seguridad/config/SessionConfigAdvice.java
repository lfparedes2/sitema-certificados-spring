package com.experto.seguridad.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.Duration;

@ControllerAdvice
public class SessionConfigAdvice {

    // Lee el valor del .yml (si no existe, usa 30 minutos por defecto)
    @Value("${server.servlet.session.timeout:30m}")
    private Duration sessionTimeout;

    @ModelAttribute("timeoutMilisegundos")
    public long getTimeoutMilisegundos() {
        return sessionTimeout.toMillis();
    }
}
