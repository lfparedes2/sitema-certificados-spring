package com.experto.seguridad.repository;

import com.experto.seguridad.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    // Busca el rol específico por su código interno (ej. "ROLE_EXTERNO")
    Optional<Rol> findByRolNombreCodigo(String rolNombreCodigo);
}
