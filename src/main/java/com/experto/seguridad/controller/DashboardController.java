package com.experto.seguridad.controller;

import com.experto.seguridad.dto.UsuarioSessionDto;
import com.experto.seguridad.util.AlertaUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping
    public String viewDashboard(HttpSession session, Model model) {
        UsuarioSessionDto userSession = (UsuarioSessionDto) session.getAttribute("USER_SESSION");
        if (userSession == null) {
            return "redirect:/auth/login";
        }

        // Código para imprimir el objeto completo en formato JSON
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userSession);
            System.out.println("=== DATOS DE USER SESSION ===");
            System.out.println(json);
            System.out.println("=============================");
        } catch (Exception e) {
            System.err.println("Error al imprimir userSession: " + e.getMessage());
        }

        model.addAttribute("user", userSession);
        return "dashboard";
    }

    @GetMapping("/usuarios")
    public String viewUsuarios(HttpSession session, Model model) {
        UsuarioSessionDto userSession = (UsuarioSessionDto) session.getAttribute("USER_SESSION");
        if (userSession == null) {
            return "redirect:/auth/login";
        }

        // Validamos si la ruta "/dashboard/usuarios" está permitida para este usuario
        boolean hasAccess = userSession.menus().stream()
                .anyMatch(menu -> "/dashboard/usuarios".equalsIgnoreCase(menu.accion()));

        if (!hasAccess) {
            model.addAttribute("errorStatus", "403 - No Autorizado");
            model.addAttribute("errorMessage", "Tu rol no tiene acceso asignado a la ruta: /dashboard/usuarios");
            model.addAttribute("user", userSession);
            return "dashboard"; // Retorna tu vista de error/dashboard principal
        }

        model.addAttribute("user", userSession);

        // Retorna la plantilla HTML física que crearemos en resources/templates/dashboard/usuarios.html
        return "dashboard/usuarios";
    }

    @GetMapping("/**")
    public String handleDynamicPages(HttpServletRequest request, HttpSession session, Model model) {
        UsuarioSessionDto userSession = (UsuarioSessionDto) session.getAttribute("USER_SESSION");
        if (userSession == null) return "redirect:/auth/login";

        String currentUri = request.getRequestURI();
        
        boolean hasAccess = userSession.menus().stream()
                .anyMatch(menu -> currentUri.equalsIgnoreCase(menu.accion()));

        if (!hasAccess) {
            model.addAttribute("errorStatus", "403 - No Autorizado");
            model.addAttribute("errorMessage", "Tu rol no tiene acceso asignado a la ruta: " + currentUri);
            model.addAttribute("user", userSession);
            return "dashboard"; 
        }

        model.addAttribute("user", userSession);
        return "dashboard"; 
    }

    @GetMapping("/usuarios/consultar")
    public String consultarUsuarioSimulado(HttpSession session, Model model) {
        UsuarioSessionDto userSession = (UsuarioSessionDto) session.getAttribute("USER_SESSION");
        if (userSession == null) {
            return "redirect:/auth/login";
        }

        // Mantenemos tu misma validación de seguridad para la ruta de usuarios
        boolean hasAccess = userSession.menus().stream()
                .anyMatch(menu -> "/dashboard/usuarios".equalsIgnoreCase(menu.accion()));

        if (!hasAccess) {
            model.addAttribute("errorStatus", "403 - No Autorizado");
            model.addAttribute("errorMessage", "Tu rol no tiene acceso asignado a esta acción.");
            model.addAttribute("user", userSession);
            return "dashboard";
        }

        // ==========================================
        // SIMULACIÓN DE CONSULTA Y LLAMADA AL BEAN
        // ==========================================
        // Aquí puedes realizar cualquier lógica de negocio simulada.
        // Inyectamos la alerta ocupando tu AlertaUtil y el AlertaDto con Lombok
        AlertaUtil.exito(model, "¡Consulta Exitosa!", "Los datos del usuario han sido verificados en el sistema.");

        model.addAttribute("user", userSession);

        // Retorna la misma plantilla física para que se muestre la alerta al cargar
        return "dashboard/usuarios";
    }

}
