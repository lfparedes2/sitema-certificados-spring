package com.experto.seguridad.controller;

import com.experto.seguridad.dto.UsuarioRegistroDto;
import com.experto.seguridad.service.RegistroUsuarioService;
import com.experto.seguridad.util.AlertaUtil;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


    @PostMapping("/register")
    public String procesarRegistro(
            @Valid @ModelAttribute("usuario") UsuarioRegistroDto usuarioDto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return VISTA_REGISTRO;
        }

        try {
            registroUsuarioService.registrarUsuario(usuarioDto);

            redirectAttributes.addFlashAttribute(
                    "mensajeExito",
                    "Los datos del usuario han sido registrados."
            );

            return "redirect:/auth/register";

        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return VISTA_REGISTRO;
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
