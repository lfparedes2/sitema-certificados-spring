package com.experto.seguridad.controller;

import com.experto.seguridad.dto.UsuarioRegistroDto;
import com.experto.seguridad.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("usuario", new UsuarioRegistroDto("", "", "", "", "", "C", "EXTERNO", "", LocalDate.now()));
        return "public/registro";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("usuario") UsuarioRegistroDto dto, BindingResult result, Model model) {
        System.out.println("[REGISTRO LOG] Petición POST recibida en el controlador para el usuario: " + dto.username());

        if (result.hasErrors()) {
            System.out.println("[REGISTRO LOG] El formulario contiene errores de validación JSR-303.");
            result.getFieldErrors().forEach(err -> System.out.println("Campo: " + err.getField() + " -> " + err.getDefaultMessage()));
            return "register";
        }

        try {
            usuarioService.registrarUsuario(dto);
            System.out.println("[REGISTRO LOG] Usuario insertado con éxito en la base de datos.");
            return "redirect:/auth/login?registered";
        } catch (IllegalArgumentException ex) {
            System.out.println("[REGISTRO LOG ERROR] Falla en lógica de negocio: " + ex.getMessage());
            model.addAttribute("error", ex.getMessage());
            return "public/registro";
        }
    }

    @PostMapping("/register2")
    public String registerUser() {

        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");

        return "public/registro";
    }

}
