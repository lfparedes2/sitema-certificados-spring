package com.experto.seguridad.repository;

import com.experto.seguridad.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsuarioUsername(String usuarioUsername);
    boolean existsByUsuarioUsername(String usuarioUsername);
    boolean existsByUsuarioEmail(String usuarioEmail);
    boolean existsByUsuarioCedula(String usuarioCedula);
}
