package com.experto.seguridad.service;

import com.experto.seguridad.model.Usuario;
import com.experto.seguridad.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("[DIAGNÓSTICO] Intentando autenticar al usuario: " + username);

        // 1. Verificar si el usuario existe en la base de datos
        Usuario usuario = usuarioRepository.findByUsuarioUsername(username)
                .orElseThrow(() -> {
                    System.out.println("[DIAGNÓSTICO ERRORES] El username '" + username + "' NO fue encontrado en la tabla seguridad.usuario.");
                    return new UsernameNotFoundException("Usuario no encontrado");
                });

        System.out.println("[DIAGNÓSTICO] Usuario encontrado exitosamente: " + usuario.getUsuarioNombre());
        System.out.println("[DIAGNÓSTICO] Estado actual del usuario: " + usuario.getUsuarioEstado());

        // 2. Verificar estado activo
        if (!"ACTIVO".equalsIgnoreCase(usuario.getUsuarioEstado())) {
            System.out.println("[DIAGNÓSTICO ERRORES] El usuario existe pero su estado NO es 'ACTIVO'. Estado actual: " + usuario.getUsuarioEstado());
            throw new UsernameNotFoundException("El usuario no está activo");
        }

        // 3. Mapear y diagnosticar roles
        var authorities = usuario.getUsuarioRoles().stream()
                .filter(ur -> {
                    boolean esActivo = "ACTIVO".equalsIgnoreCase(ur.getUsuarioRolEstado());
                    if (!esActivo) {
                        System.out.println("[DIAGNÓSTICO ADVERTENCIA] Se omitió el rol " + ur.getRol().getRolNombreCodigo() + " porque la relación usuario_rol está como: " + ur.getUsuarioRolEstado());
                    }
                    return esActivo;
                })
                .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRol().getRolNombreCodigo().toUpperCase()))
                .toList();

        System.out.println("[DIAGNÓSTICO] Cantidad de roles activos cargados: " + authorities.size());
        authorities.forEach(auth -> System.out.println("[DIAGNÓSTICO] Rol asignado: " + auth.getAuthority()));

        if (authorities.isEmpty()) {
            System.out.println("[DIAGNÓSTICO ERRORES] El usuario no tiene ningún rol activo en la tabla usuario_rol. Spring Security rechazará la sesión.");
        }

        // Devolvemos el usuario para que Spring valide la contraseña cifrada
        return new User(usuario.getUsuarioUsername(), usuario.getUsuarioContrasenia(), authorities);
    }
}
