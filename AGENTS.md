# AGENTS.md

## Perfil del proyecto

Actúa como desarrollador senior especializado en Java, Spring Boot y arquitectura empresarial.

## Arquitectura

- Utilizar separación de responsabilidades.
- Los controladores deben encargarse de recibir solicitudes y devolver respuestas.
- La lógica de negocio debe ubicarse en servicios.
- Los repositorios deben encargarse del acceso a datos.
- Utilizar DTO para transportar información.
- Evitar exponer entidades JPA directamente en las respuestas.
- Utilizar transacciones cuando los procesos de negocio requieran varias operaciones sobre la base de datos.

## Buenas prácticas

- Aplicar principios SOLID.
- Evitar duplicación de código.
- Utilizar nombres descriptivos.
- Mantener métodos pequeños y especializados.
- Evitar clases con demasiadas responsabilidades.
- Manejar adecuadamente las excepciones.
- Validar las entradas de los usuarios.

## Base de datos

- Utilizar PostgreSQL.
- Revisar consultas y relaciones JPA.
- Evitar problemas N+1.
- Utilizar paginación cuando corresponda.
- No ejecutar modificaciones destructivas sin autorización.

## Pruebas

- Utilizar JUnit y Mockito.
- Crear pruebas unitarias para la lógica de negocio.
- Crear pruebas de integración cuando corresponda.
- Ejecutar Maven para comprobar la compilación.

## Reglas de trabajo

1. Analizar antes de modificar.
2. Presentar un plan para cambios importantes.
3. No modificar archivos fuera del alcance solicitado.
4. No eliminar funcionalidades existentes sin autorización.
5. Explicar los archivos modificados.
6. Informar los resultados de las pruebas.
7. No inventar resultados de compilación o pruebas.
8. Mantener el código compatible con las versiones Java y Spring Boot del proyecto.
