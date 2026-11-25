INSERT INTO seguridad.aplicacion (id_aplicacion, aplicacion_descripcion, aplicacion_nombre_codigo, aplicacion_nombre_visible)
VALUES (1, 'Modulo Central de Seguridad y Control de Accesos', 'APP_CORE', 'Sistema de Gestion Institucional')
ON CONFLICT (id_aplicacion) DO NOTHING;

INSERT INTO seguridad.menu (id_menu, menu_accion, menu_estado, menu_etiqueta, menu_img, menu_nivel, menu_orden, menu_tipo, id_aplicacion, menu_id_padre) VALUES
(1, '/dashboard', 'ACTIVO', 'Inicio', '<svg class="w-5 h-5 stroke-current" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" /></svg>', 1, 1, 'INTERNO', 1, NULL),
(2, '/dashboard/usuarios', 'ACTIVO', 'Usuarios', '<svg xmlns="http://w3.org" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6"><path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z" /></svg>', 1, 2, 'INTERNO', 1, NULL),
(3, '/dashboard/reportes', 'ACTIVO', 'Reportes', '<svg xmlns="http://w3.org" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6"><path stroke-linecap="round" stroke-linejoin="round" d="M7.5 14.25v2.25m3-4.5v4.5m3-6.75v6.75m3-9v9M6 20.25h12A2.25 2.25 0 0 0 20.25 18V6A2.25 2.25 0 0 0 18 3.75H6A2.25 2.25 0 0 0 3.75 6v12A2.25 2.25 0 0 0 6 20.25Z" /></svg>', 1, 3, 'INTERNO', 1, NULL),
(4, '/dashboard/configuracion', 'ACTIVO', 'Configuracion', '<svg xmlns="http://w3.org" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6"><path stroke-linecap="round" stroke-linejoin="round" d="M11.42 15.17 17.25 21A2.652 2.652 0 0 0 21 17.25l-5.877-5.877M11.42 15.17l2.496-3.03c.317-.384.74-.626 1.208-.766M11.42 15.17l-4.655 5.653a2.548 2.548 0 1 1-3.586-3.586l6.837-5.63m5.108-.233c.55-.164 1.163-.188 1.743-.14a4.5 4.5 0 0 0 4.486-6.336l-3.276 3.277a3.004 3.004 0 0 1-2.25-2.25l3.276-3.276a4.5 4.5 0 0 0-6.336 4.486c.091 1.076-.071 2.264-.904 2.95l-.102.085m-1.745 1.437L5.909 7.5H4.5L2.25 3.75l1.5-1.5L7.5 4.5v1.409l4.26 4.26m-1.745 1.437 1.745-1.437m6.615 8.206L15.75 15.75M4.867 19.125h.008v.008h-.008v-.008Z" /></svg>', 1, 4, 'INTERNO', 1, NULL),
(5, '/dashboard/excel', 'ACTIVO', 'Reporte Excel', '<svg xmlns="http://w3.org" viewBox="0 0 16 16" fill="currentColor" class="size-4"><path fill-rule="evenodd" d="M15 11a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2v6ZM7.25 7.5a.5.5 0 0 0-.5-.5H3a.5.5 0 0 0-.5.5V8a.5.5 0 0 0 .5.5h3.75a.5.5 0 0 0 .5-.5v-.5Zm1.5 3a.5.5 0 0 1 .5-.5H13a.5.5 0 0 1 .5.5v.5a.5.5 0 0 1-.5.5H9.25a.5.5 0 0 1-.5-.5v-.5ZM13.5 8v-.5A.5.5 0 0 0 13 7H9.25a.5.5 0 0 0-.5.5V8a.5.5 0 0 0 .5.5H13a.5.5 0 0 0 .5-.5Zm-6.75 3.5a.5.5 0 0 0 .5-.5v-.5a.5.5 0 0 0-.5-.5H3a.5.5 0 0 0-.5.5v.5a.5.5 0 0 0 .5.5h3.75Z" clip-rule="evenodd" /></svg>', 2, 5, 'INTERNO', 1, 3)
ON CONFLICT (id_menu) DO NOTHING;

INSERT INTO seguridad.rol (id_rol, rol_descripcion, rol_nombre_codigo, rol_nombre_visible, id_aplicacion) VALUES
(1, 'Administrador Total del Sistema', 'ADMIN', 'Administrador', 1),
(2, 'Usuario externo', 'USER', 'Usuario', 1)
ON CONFLICT (id_rol) DO NOTHING;

INSERT INTO seguridad.rol_menu (id_rol_menu, id_menu, id_rol) VALUES
(1, 1, 1),
(2, 2, 1),
(3, 3, 1),
(4, 4, 1),
(5, 1, 2),
(6, 5, 1)
ON CONFLICT (id_rol_menu) DO NOTHING;

INSERT INTO seguridad.usuario (id_usuario, fecha_caducida_pasaporte, id_funcionario, inicial, tipo_identificacion, usuario_cedula, usuario_contrasenia, usuario_email, usuario_estado, usuario_nombre, usuario_tipo, usuario_username, fecha_creacion, fecha_caduca_pasword, code_verifica) VALUES
(1, '2030-12-31', NULL, 'ADM', 'C', '1725896341', '$2a$12$5S/1s0YFemMbaSGLNda5JObFgoalem2/wSwaoowwlzG2WHayclAzK', 'admin@institucion.gob.ec', 'ACTIVO', 'Administrador Central de Seguridad', 'INTERNO', 'admin', NULL, '2026-12-31 00:00:00-05', NULL),
(2, NULL, NULL, 'USR', 'C', '123123123', '$2a$12$5S/1s0YFemMbaSGLNda5JObFgoalem2/wSwaoowwlzG2WHayclAzK', 'luis@uafe.gob', 'ACTIVO', 'LUIS FERNANDO PAREDES PAREDES', 'EXTERNO', 'prueba', NULL, NULL, NULL)
ON CONFLICT (id_usuario) DO NOTHING;

INSERT INTO seguridad.usuario_rol (id_usuario_rol, usuario_rol_estado, id_rol, id_usuario) VALUES
(1, 'ACTIVO', 1, 1),
(2, 'ACTIVO', 2, 2)
ON CONFLICT (id_usuario_rol) DO NOTHING;

SELECT setval('seguridad.aplicacion_id_aplicacion_seq', COALESCE((SELECT MAX(id_aplicacion) FROM seguridad.aplicacion), 1), true);
SELECT setval('seguridad.menu_id_menu_seq', COALESCE((SELECT MAX(id_menu) FROM seguridad.menu), 1), true);
SELECT setval('seguridad.rol_id_rol_seq', COALESCE((SELECT MAX(id_rol) FROM seguridad.rol), 1), true);
SELECT setval('seguridad.rol_menu_id_rol_menu_seq', COALESCE((SELECT MAX(id_rol_menu) FROM seguridad.rol_menu), 1), true);
SELECT setval('seguridad.usuario_id_usuario_seq', COALESCE((SELECT MAX(id_usuario) FROM seguridad.usuario), 1), true);
SELECT setval('seguridad.usuario_rol_id_usuario_rol_seq', COALESCE((SELECT MAX(id_usuario_rol) FROM seguridad.usuario_rol), 1), true);
