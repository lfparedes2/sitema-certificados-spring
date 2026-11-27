package com.experto.seguridad.enums;

public enum Estado {

    ACTIVO("A", "ACTIVO"),
    INACTIVO("I", "INACTIVO");

    private final String codigo;
    private final String descripcion;

    Estado(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static Estado fromCodigo(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (Estado estado : values()) {
            if (estado.codigo.equalsIgnoreCase(codigo)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Código de estado no válido: " + codigo);
    }
}
