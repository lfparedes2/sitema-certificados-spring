package com.experto.seguridad.controller;

import com.experto.seguridad.dto.UsuarioRegistroDto;
import com.experto.seguridad.service.RegistroUsuarioService;
import com.experto.seguridad.util.AlertaUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final String VISTA_LOGIN = "login";
    private static final String VISTA_REGISTRO = "public/registro";
    private static final String FRAGMENTO_FORMULARIO = "public/auth/register :: formularioRegistro";


    private final RegistroUsuarioService registroUsuarioService;

    public AuthController(RegistroUsuarioService registroUsuarioService) {
        this.registroUsuarioService = registroUsuarioService;
    }


    @GetMapping("/login")
    public String login() {
        return VISTA_LOGIN;
    }

    @GetMapping("/register")
    public String mostrarFormulario(Model model) {
        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", UsuarioRegistroDto.vacio());
        }
        return VISTA_REGISTRO;
    }

    /*@PostMapping("/register")
    public String procesarRegistro(@Valid @ModelAttribute("usuario") UsuarioRegistroDto dto, BindingResult result, Model model) {
        System.out.println("[REGISTRO LOG] Petición POST recibida en el controlador para el usuario: " + dto.username());

        if (result.hasErrors()) {
            System.out.println("[REGISTRO LOG] El formulario contiene errores de validación JSR-303.");
            result.getFieldErrors().forEach(err -> System.out.println("Campo: " + err.getField() + " -> " + err.getDefaultMessage()));
            return VISTA_REGISTRO;
        }

        try {
            usuarioService.registrarUsuario(dto);
            System.out.println("[REGISTRO LOG] Usuario insertado con éxito en la base de datos.");
            return "redirect:/auth/login?registered";
        } catch (IllegalArgumentException ex) {
            System.out.println("[REGISTRO LOG ERROR] Falla en lógica de negocio: " + ex.getMessage());
            model.addAttribute("error", ex.getMessage());
            return VISTA_REGISTRO;
        }
    }*/

    @PostMapping("/register")
    public String procesarRegistro(
            @Valid @ModelAttribute("usuario") UsuarioRegistroDto usuarioDto,
            BindingResult result,
            @RequestHeader(value = "HX-Request", required = false) boolean isHtmx,
            HttpServletResponse response,
            Model model) {

        if (result.hasErrors()) {
            return isHtmx ? FRAGMENTO_FORMULARIO : VISTA_REGISTRO;
        }

        try {
            registroUsuarioService.registrarUsuario(usuarioDto);

            AlertaUtil.exito(model, "¡Registro Exitoso!", "Los datos del usuario han sido registrados. Redirigiendo al inicio de sesión...");
            model.addAttribute("redirigirLogin", true);

            // Devolvemos el fragmento para que se renderice la alerta en pantalla
            return isHtmx ? FRAGMENTO_FORMULARIO : VISTA_REGISTRO;

        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return isHtmx ? FRAGMENTO_FORMULARIO : VISTA_REGISTRO;
        }
    }

    @PostMapping("/register/consultar")
    public String consultarUsuarioSimulado(Model model) {
        System.out.println("Entra ....");
        // Usa AlertaUtil.error() porque vas a retornar la vista directamente sin "redirect:"
        AlertaUtil.error(
                model,
                "Error al guardar",
                "No se pudo registrar el usuario.",
                3000,
                "#d33"
        );

        // Retorna el nombre de tu vista HTML actual (sin "redirect:")
        return "public/registro";
    }

}
