package com.experto.seguridad.controller;

import com.experto.seguridad.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/auth")
public class TwoFactorAuthController {

    private final UsuarioService usuarioService;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public TwoFactorAuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/verify-2fa")
    public String showVerifyPage(HttpSession session) {
        // Si no hay una sesión temporal en proceso, lo regresamos al login
        if (session.getAttribute("TEMP_AUTH") == null) {
            return "redirect:/auth/login";
        }
        return "verify-2fa";
    }

    @PostMapping("/verify-2fa")
    public String verifyCode(@RequestParam("codigo") String codigoIngresado,
                             HttpSession session,
                             RedirectAttributes attributes,
                             HttpServletRequest request,
                             HttpServletResponse response) {

        Authentication tempAuth = (Authentication) session.getAttribute("TEMP_AUTH");
        String correctCode = (String) session.getAttribute("2FA_CODE");
        LocalDateTime expiry = (LocalDateTime) session.getAttribute("2FA_EXPIRY");
        Integer attempts = (Integer) session.getAttribute("2FA_ATTEMPTS");

        if (tempAuth == null || correctCode == null) {
            return "redirect:/auth/login";
        }

        // 1. Validar Tiempo (5 minutos máximo)
        if (LocalDateTime.now().isAfter(expiry)) {
            session.invalidate();
            attributes.addFlashAttribute("error2fa", "El código ha expirado después de 5 minutos. Vuelva a iniciar sesión.");
            return "redirect:/auth/login"; // Lo mandamos al login para que reciba un nuevo código
        }

        // 2. Validar Intentos
        if (attempts >= 3) {
            session.invalidate();
            attributes.addFlashAttribute("error2fa", "Su cuenta ha sido bloqueada temporalmente por superar los 3 intentos fallidos del código.");
            return "redirect:/auth/login";
        }

        // 3. Validar el Código
        if (!correctCode.equals(codigoIngresado)) {
            attempts++;
            session.setAttribute("2FA_ATTEMPTS", attempts);

            if (attempts >= 3) {
                session.invalidate();
                attributes.addFlashAttribute("error2fa", "Ha fallado 3 veces. Se ha abortado el inicio de sesión por seguridad.");
                return "redirect:/auth/login";
            }

            int restantes = 3 - attempts;
            attributes.addFlashAttribute("error2fa", "Código incorrecto. Te quedan " + restantes + " intentos.");
            return "redirect:/auth/verify-2fa";
        }

        // 4. ÉXITO: Restaurar la sesión oficial de Spring Security 6
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(tempAuth);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);

        // 5. Cargar los datos del DTO (los mismos que hacías antes)
        var dto = usuarioService.obtenerDatosSesion(tempAuth.getName());
        session.setAttribute("USER_SESSION", dto);

        // Limpiar variables temporales por seguridad
        session.removeAttribute("TEMP_AUTH");
        session.removeAttribute("2FA_CODE");
        session.removeAttribute("2FA_EXPIRY");
        session.removeAttribute("2FA_ATTEMPTS");

        return "redirect:/dashboard";
    }
}