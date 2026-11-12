package com.experto.seguridad.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "email_evento", schema = "seguridad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mail_evento")
    private Integer idMailEvento;

    @Column(name = "codigo", length = 32, nullable = false)
    private String codigo;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @Column(name = "asunto", length = 200, nullable = false)
    private String asunto;

    @Column(name = "email_remitente", length = 100)
    private String emailRemitente;

    @Column(name = "estado", length = 1, nullable = false)
    private String estado;

    @Column(name = "plantilla", columnDefinition = "TEXT")
    private String plantilla;

}
