package com.experto.seguridad.dto;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
public class AlertaDto {
    private String tipo;    // 'success', 'error', 'warning', 'info'
    private String titulo;
    private String mensaje;
    private Integer tiempo;  // en milisegundos (ej: 3000), o null para permanente
    private String colorBoton;

    // Constructor completo
    public AlertaDto(String tipo, String titulo, String mensaje, Integer tiempo, String colorBoton) {
        this.tipo = tipo;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.tiempo = tiempo;
        this.colorBoton = colorBoton != null ? colorBoton : "#3085d6"; // Color por defecto
    }
}