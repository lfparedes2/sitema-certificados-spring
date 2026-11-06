package com.experto.seguridad.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_rol", schema = "seguridad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"usuario", "rol"})
@EqualsAndHashCode(exclude = {"usuario", "rol"})
public class UsuarioRol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_rol")
    private Long idUsuarioRol;

    @Column(name = "usuario_rol_estado", nullable = false, length = 50)
    private String usuarioRolEstado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, foreignKey = @ForeignKey(name = "fk_94qjvtxfc9knu3b2yo0wwmywr"))
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", nullable = false, foreignKey = @ForeignKey(name = "fk_4owh0vmy12av0j3yj2hb7e8qb"))
    private Rol rol;
}
