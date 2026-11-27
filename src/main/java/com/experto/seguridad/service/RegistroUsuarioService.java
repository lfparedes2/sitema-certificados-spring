package com.experto.seguridad.service;

import com.experto.seguridad.dto.UsuarioRegistroDto;
import com.experto.seguridad.enums.Estado;
import com.experto.seguridad.enums.TipoUsuario;
import com.experto.seguridad.model.Usuario;
import com.experto.seguridad.repository.UsuarioRepository;
import com.experto.seguridad.util.CodigoGeneratorUtil;
import com.experto.seguridad.util.IdentificacionEcuadorValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Service
public class RegistroUsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    public RegistroUsuarioService(/* UsuarioRepository usuarioRepository, */ PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository) {
        // this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void registrarUsuario(UsuarioRegistroDto dto) {
        // 1. Delegar validación de Cédula/RUC/Pasaporte al utilitario
        IdentificacionEcuadorValidator.validar(dto.tipoIdentificacion(), dto.cedula());

        // 2. Validar duplicados en BD
        /*
        if (usuarioRepository.existsByCedula(dto.cedula())) {
            throw new IllegalArgumentException("El número de identificación ya se encuentra registrado.");
        }
        if (usuarioRepository.existsByEmail(dto.email().toLowerCase())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }
        */

        // 3. Generar códigos usando el utilitario
        String claveTemporal = CodigoGeneratorUtil.generarClaveTemporal(8);
        int codigoVerificacion = CodigoGeneratorUtil.generarNumerico(4);

        // 4. Guardar entidad en BD

        Usuario usuario = new Usuario();
        usuario.setUsuarioUsername(dto.cedula().trim());
        usuario.setUsuarioCedula(dto.cedula().trim());
        usuario.setUsuarioNombre(dto.nombre().trim().toUpperCase().trim());
        usuario.setUsuarioEmail(dto.email().trim().toLowerCase());
        usuario.setTipoIdentificacion(dto.tipoIdentificacion());
        usuario.setUsuarioTipo(dto.tipo() != null ? dto.tipo() : TipoUsuario.INTERNO.toString());
        usuario.setUsuarioContrasenia(passwordEncoder.encode(claveTemporal));
        usuario.setCodeVerifica(codigoVerificacion);
        usuario.setUsuarioEstado(Estado.ACTIVO.getDescripcion());
        usuario.setFechaCreacion(LocalDate.now());
        usuario.setFechaCaducaPasword(ZonedDateTime.now().plusDays(1));

        usuarioRepository.save(usuario);


        // 5. Enviar notificación por correo con claveTemporal y codigoVerificacion
    }


}
