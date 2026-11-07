package com.experto.seguridad.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public record UsuarioSessionDto(
        Long idUsuario,
        String username,
        String nombre,
        String email,
        String cedula,
        String tipo,
        LocalDate fechaCaducidaPasaporte,
        List<String> rolesCodigos,
        List<MenuInfo> menus
) implements Serializable {
    public record MenuInfo(
            Long idMenu,
            String etiqueta,
            String accion,
            String img,
            Integer orden,
            Long idMenuPadre
    ) implements Serializable {}
}