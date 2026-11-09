package com.experto.seguridad.config;

import com.experto.seguridad.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioService usuarioService;

    @Value("${seguridad.2fa.habilitado:true}")
    private boolean is2faEnabled;

    public CustomAuthenticationSuccessHandler(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();

        if (is2faEnabled) {
            // 1. Generar código de 4 dígitos (0000 a 9999)
            String code = String.format("%04d", new Random().nextInt(10000));

            // TODO: Aquí enviarías el correo o SMS real. Por ahora lo imprimimos en consola.
            System.out.println("=========================================");
            System.out.println("[2FA] CÓDIGO DE VERIFICACIÓN PARA " + authentication.getName() + ": " + code);
            System.out.println("=========================================");

            // 2. Guardar datos en la sesión temporal (5 minutos de vida, 0 intentos)
            session.setAttribute("TEMP_AUTH", authentication);
            session.setAttribute("2FA_CODE", code);
            session.setAttribute("2FA_EXPIRY", LocalDateTime.now().plusMinutes(5));
            session.setAttribute("2FA_ATTEMPTS", 0);

            // 3. Limpiar el contexto de seguridad para que NO esté autenticado aún
            SecurityContextHolder.clearContext();

            // 4. Redirigir a la pantalla de ingreso del código
            response.sendRedirect("/auth/verify-2fa");
        } else {
            // Flujo normal directo si el 2FA está apagado
            var dto = usuarioService.obtenerDatosSesion(authentication.getName());
            session.setAttribute("USER_SESSION", dto);
            response.sendRedirect("/dashboard");
        }
    }
}