package com.experto.seguridad.service;

import com.experto.seguridad.dto.UsuarioRegistroDto;
import com.experto.seguridad.dto.UsuarioSessionDto;
import com.experto.seguridad.model.Usuario;
import com.experto.seguridad.repository.RolRepository;
import com.experto.seguridad.repository.UsuarioRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Usamos @Lazy en el PasswordEncoder para romper de forma definitiva la referencia circular
    public UsuarioService(UsuarioRepository usuarioRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registrarUsuario(UsuarioRegistroDto dto) {
        if (usuarioRepository.existsByUsuarioUsername(dto.username())) throw new IllegalArgumentException("Username ya registrado");
        if (usuarioRepository.existsByUsuarioEmail(dto.email())) throw new IllegalArgumentException("Email ya registrado");
        if (usuarioRepository.existsByUsuarioCedula(dto.cedula())) throw new IllegalArgumentException("Cédula ya registrada");

        Usuario usuario = Usuario.builder()
                .usuarioUsername(dto.username())
                .usuarioCedula(dto.cedula())
                .usuarioContrasenia(passwordEncoder.encode(dto.contrasenia()))
                .usuarioEmail(dto.email())
                .usuarioNombre(dto.nombre())
                .usuarioTipo(dto.tipo())
                .usuarioEstado("ACTIVO")
                .usuarioRoles(new HashSet<>())
                .build();

        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioSessionDto obtenerDatosSesion(String username) {
        Usuario usuario = usuarioRepository.findByUsuarioUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no hallado"));

        List<String> roles = usuario.getUsuarioRoles().stream()
                .filter(ur -> "ACTIVO".equalsIgnoreCase(ur.getUsuarioRolEstado()))
                .map(ur -> ur.getRol().getRolNombreCodigo())
                .toList();

        List<UsuarioSessionDto.MenuInfo> menus = usuario.getUsuarioRoles().stream()
                .filter(ur -> "ACTIVO".equalsIgnoreCase(ur.getUsuarioRolEstado()))
                .flatMap(ur -> ur.getRol().getRolMenus().stream())
                .map(rm -> rm.getMenu())
                .filter(m -> "ACTIVO".equalsIgnoreCase(m.getMenuEstado()))
                .distinct()
                .map(m -> new UsuarioSessionDto.MenuInfo(
                        m.getIdMenu(),                                                   // 1. idMenu (Nuevo)
                        m.getMenuEtiqueta(),
                        m.getMenuAccion(),
                        m.getMenuImg(),
                        m.getMenuOrden(),
                        m.getMenuPadre() != null ? m.getMenuPadre().getIdMenu() : null   // 2. idMenuPadre (Nuevo)
                ))
                .sorted((m1, m2) -> Integer.compare(m1.orden() != null ? m1.orden() : 0, m2.orden() != null ? m2.orden() : 0))
                .toList();

        return new UsuarioSessionDto(
                usuario.getIdUsuario(), usuario.getUsuarioUsername(), usuario.getUsuarioNombre(),
                usuario.getUsuarioEmail(), usuario.getUsuarioCedula(), usuario.getUsuarioTipo(), roles, menus
        );
    }

}
