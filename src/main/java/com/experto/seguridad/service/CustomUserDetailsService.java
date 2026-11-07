package com.experto.seguridad.service;

import com.experto.seguridad.model.Usuario;
import com.experto.seguridad.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Value("${seguridad.password.validar-caducidad:true}")
    private boolean validarCaducidad;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("======================================================");
        System.out.println("[LOGIN] Intentando autenticar a: " + username);

        Usuario usuario = usuarioRepository.findByUsuarioUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (!"ACTIVO".equalsIgnoreCase(usuario.getUsuarioEstado())) {
            throw new UsernameNotFoundException("El usuario no está activo");
        }

        // ====================================================================================
        // LÓGICA DE CADUCIDAD CON LOGS PARA TERMINAL
        // ====================================================================================
        boolean credentialsExpired = false;

        System.out.println("\n--- INICIO VALIDACION CADUCIDAD ---");
        System.out.println("1. Usuario a validar: " + username);
        System.out.println("2. Bandera validarCaducidad (application.yml): " + validarCaducidad);

        if (validarCaducidad) {
            ZonedDateTime fechaCaducidad = usuario.getFechaCaducaPasword();
            ZonedDateTime fechaActual = ZonedDateTime.now();

            System.out.println("3. Fecha de caducidad leída en BD: " + fechaCaducidad);
            System.out.println("4. Fecha y hora actual del sistema: " + fechaActual);

            if (fechaCaducidad == null) {
                System.out.println("RESULTADO: La fecha en BD es NULL. Dejando pasar por defecto.");
            } else if (fechaCaducidad.isBefore(fechaActual)) {
                System.out.println("RESULTADO: ¡La fecha expiró! Marcando credenciales como caducadas...");
                credentialsExpired = true;
            } else {
                System.out.println("RESULTADO: Fecha válida (está en el futuro).");
            }
        } else {
            System.out.println("RESULTADO: Validación apagada desde application.yml");
        }
        System.out.println("--- FIN VALIDACION CADUCIDAD ---\n");
        // ====================================================================================

        var authorities = usuario.getUsuarioRoles().stream()
                .filter(ur -> "ACTIVO".equalsIgnoreCase(ur.getUsuarioRolEstado()))
                .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRol().getRolNombreCodigo().toUpperCase()))
                .toList();

        System.out.println("======================================================");

        return User.builder()
                .username(usuario.getUsuarioUsername())
                .password(usuario.getUsuarioContrasenia())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(credentialsExpired) // <--- Activa la excepción CredentialsExpiredException de forma limpia
                .disabled(false)
                .build();
    }
}