package com.experto.seguridad.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "rol", schema = "seguridad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"aplicacion", "rolMenus"})
@EqualsAndHashCode(exclude = {"aplicacion", "rolMenus"})
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    @Column(name = "rol_descripcion", length = 200)
    private String rolDescripcion;

    @Column(name = "rol_nombre_codigo", nullable = false, length = 100)
    private String rolNombreCodigo;

    @Column(name = "rol_nombre_visible", nullable = false, length = 150)
    private String rolNombreVisible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aplicacion", nullable = false, foreignKey = @ForeignKey(name = "fk_lgycvhxub75b5k7sbdlps5uh7"))
    private Aplicacion aplicacion;

    @OneToMany(mappedBy = "rol", fetch = FetchType.EAGER)
    private Set<RolMenu> rolMenus;
}
