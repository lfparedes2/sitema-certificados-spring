package com.experto.seguridad.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rol_menu", schema = "seguridad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"rol", "menu"})
@EqualsAndHashCode(exclude = {"rol", "menu"})
public class RolMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol_menu")
    private Long idRolMenu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", nullable = false, foreignKey = @ForeignKey(name = "fk_3jv2jmcior6uwy5u8i4xc5u68"))
    private Rol rol;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_menu", nullable = false, foreignKey = @ForeignKey(name = "fk_rc80a4yd36m6j0l4on6s4dtg9"))
    private Menu menu;
}
