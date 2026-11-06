package com.experto.seguridad.model;

import jakarta.persistence.*;
import lombok.*;


import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "menu", schema = "seguridad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"aplicacion", "menuPadre", "submenus"})
@EqualsAndHashCode(exclude = {"aplicacion", "menuPadre", "submenus"})
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_menu")
    private Long idMenu;

    @Column(name = "menu_accion", length = 100)
    private String menuAccion;

    @Column(name = "menu_estado", nullable = false, length = 50)
    private String menuEstado;

    @Column(name = "menu_etiqueta", length = 100)
    private String menuEtiqueta;

    @Column(name = "menu_img", length = 1000)
    private String menuImg;

    @Column(name = "menu_nivel")
    private Integer menuNivel;

    @Column(name = "menu_orden")
    private Integer menuOrden;

    @Column(name = "menu_tipo", nullable = false, length = 50)
    private String menuTipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aplicacion", nullable = false, foreignKey = @ForeignKey(name = "fk_1ck9wgckd2qr09mvjndykfouj"))
    private Aplicacion aplicacion;

    // Relación con el Padre (Mantenemos LAZY por rendimiento)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id_padre", foreignKey = @ForeignKey(name = "fk_hk699jccuapyqpgavkdr127vr"))
    private Menu menuPadre;

    // SOLUCIÓN: Agregamos la lista de hijos (Submenús)
    // Usamos EAGER para asegurar que Thymeleaf pueda leer los submenús sin dar LazyInitializationException
    @OneToMany(mappedBy = "menuPadre", fetch = FetchType.EAGER)
    @OrderBy("menuOrden ASC") // Te los ordena automáticamente por tu columna de orden
    private List<Menu> submenus;
}
