package com.experto.seguridad.repository;

import com.experto.seguridad.model.EmailEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailEventoRepository extends JpaRepository<EmailEvento, Integer> {

    Optional<EmailEvento> findByCodigoAndEstado(String codigo, String estado);
}