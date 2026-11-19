package com.experto.seguridad.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

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
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
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
    ) implements Serializable {
    }
}