package com.experto.seguridad.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "usuario", schema = "seguridad", uniqueConstraints = {
    @UniqueConstraint(name = "Uk_ceduka", columnNames = {"usuario_cedula"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "usuarioRoles")
@EqualsAndHashCode(exclude = "usuarioRoles")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "usuario_cedula", length = 15)
    private String usuarioCedula;

    @Column(name = "usuario_contrasenia", length = 300)
    private String usuarioContrasenia;

    @Column(name = "usuario_email", nullable = false, length = 50)
    private String usuarioEmail;

    @Column(name = "usuario_estado", nullable = false, length = 50)
    private String usuarioEstado;

    @Column(name = "id_funcionario")
    private Integer idFuncionario;

    @Column(name = "usuario_nombre", length = 100)
    private String usuarioNombre;

    @Column(name = "usuario_tipo", nullable = false, length = 50)
    private String usuarioTipo;

    @Column(name = "usuario_username", nullable = false, length = 50)
    private String usuarioUsername;

    @Column(name = "fecha_caducida_pasaporte")
    private LocalDate fechaCaducidaPasaporte;

    @Column(name = "tipo_identificacion", length = 2)
    private String tipoIdentificacion;

    @Column(name = "inicial", length = 3)
    private String inicial;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @Column(name = "fecha_caduca_pasword")
    private ZonedDateTime fechaCaducaPasword;

    @Column(name = "code_verifica")
    private Integer codeVerifica;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    private Set<UsuarioRol> usuarioRoles;
}
