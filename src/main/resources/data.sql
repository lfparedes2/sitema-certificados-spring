CREATE SCHEMA IF NOT EXISTS seguridad;

INSERT INTO seguridad.aplicacion (id_aplicacion, aplicacion_descripcion, aplicacion_nombre_codigo, aplicacion_nombre_visible) VALUES (1, 'Modulo Central de Seguridad y Control de Accesos', 'APP_CORE', 'Sistema de Gestion Institucional') ON CONFLICT (id_aplicacion) DO NOTHING;

INSERT INTO seguridad.menu (id_menu, menu_accion, menu_estado, menu_etiqueta, menu_img, menu_nivel, menu_orden, menu_tipo, id_aplicacion, menu_id_padre) VALUES (1, '/dashboard', 'ACTIVO', 'Inicio Dashboard', 'home.png', 1, 1, 'INTERNO', 1, NULL) ON CONFLICT (id_menu) DO NOTHING;
INSERT INTO seguridad.menu (id_menu, menu_accion, menu_estado, menu_etiqueta, menu_img, menu_nivel, menu_orden, menu_tipo, id_aplicacion, menu_id_padre) VALUES (2, '/dashboard/usuarios', 'ACTIVO', 'Gestion de Usuarios', 'users.png', 1, 2, 'INTERNO', 1, NULL) ON CONFLICT (id_menu) DO NOTHING;
INSERT INTO seguridad.menu (id_menu, menu_accion, menu_estado, menu_etiqueta, menu_img, menu_nivel, menu_orden, menu_tipo, id_aplicacion, menu_id_padre) VALUES (3, '/dashboard/reportes', 'ACTIVO', 'Reportes del Sistema', 'chart.png', 1, 3, 'INTERNO', 1, NULL) ON CONFLICT (id_menu) DO NOTHING;
INSERT INTO seguridad.menu (id_menu, menu_accion, menu_estado, menu_etiqueta, menu_img, menu_nivel, menu_orden, menu_tipo, id_aplicacion, menu_id_padre) VALUES (4, '/dashboard/configuracion', 'ACTIVO', 'Configuracion Avanzada', 'settings.png', 1, 4, 'INTERNO', 1, NULL) ON CONFLICT (id_menu) DO NOTHING;

INSERT INTO seguridad.rol (id_rol, rol_descripcion, rol_nombre_codigo, rol_nombre_visible, id_aplicacion) VALUES (1, 'Administrador Total del Sistema', 'ADMIN', 'Administrador', 1) ON CONFLICT (id_rol) DO NOTHING;
INSERT INTO seguridad.rol (id_rol, rol_descripcion, rol_nombre_codigo, rol_nombre_visible, id_aplicacion) VALUES (2, 'Operador Estandar de Consultas', 'OPERADOR', 'Operador', 1) ON CONFLICT (id_rol) DO NOTHING;

INSERT INTO seguridad.rol_menu (id_rol_menu, id_rol, id_menu) VALUES (1, 1, 1) ON CONFLICT (id_rol_menu) DO NOTHING;
INSERT INTO seguridad.rol_menu (id_rol_menu, id_rol, id_menu) VALUES (2, 1, 2) ON CONFLICT (id_rol_menu) DO NOTHING;
INSERT INTO seguridad.rol_menu (id_rol_menu, id_rol, id_menu) VALUES (3, 1, 3) ON CONFLICT (id_rol_menu) DO NOTHING;
INSERT INTO seguridad.rol_menu (id_rol_menu, id_rol, id_menu) VALUES (4, 1, 4) ON CONFLICT (id_rol_menu) DO NOTHING;
INSERT INTO seguridad.rol_menu (id_rol_menu, id_rol, id_menu) VALUES (5, 2, 1) ON CONFLICT (id_rol_menu) DO NOTHING;
INSERT INTO seguridad.rol_menu (id_rol_menu, id_rol, id_menu) VALUES (6, 2, 3) ON CONFLICT (id_rol_menu) DO NOTHING;

INSERT INTO seguridad.usuario (id_usuario, usuario_cedula, usuario_contrasenia, usuario_email, usuario_estado, id_funcionario, usuario_nombre, usuario_tipo, usuario_username, fecha_caducida_pasaporte, tipo_identificacion, inicial) VALUES (1, '1725896341', '$2a$12$RqyV2rVq9b4kIeUa7QeEKeM3Bskm01i6n2j/iA0JbHvxmO5Ffe5l2', 'admin@institucion.gob.ec', 'ACTIVO', 101, 'Administrador Central de Seguridad', 'INTERNO', 'admin', '2030-12-31', 'C', 'ADM') ON CONFLICT (id_usuario) DO NOTHING;

INSERT INTO seguridad.usuario_rol (id_usuario_rol, usuario_rol_estado, id_rol, id_usuario) VALUES (1, 'ACTIVO', 1, 1) ON CONFLICT (id_usuario_rol) DO NOTHING;
