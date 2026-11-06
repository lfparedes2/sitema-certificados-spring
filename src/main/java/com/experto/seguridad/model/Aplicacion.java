package com.experto.seguridad.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "aplicacion", schema = "seguridad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aplicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aplicacion")
    private Long idAplicacion;

    @Column(name = "aplicacion_descripcion", length = 200)
    private String aplicacionDescripcion;

    @Column(name = "aplicacion_nombre_codigo", nullable = false, length = 100)
    private String aplicacionNombreCodigo;

    @Column(name = "aplicacion_nombre_visible", nullable = false, length = 100)
    private String aplicacionNombreVisible;
}
