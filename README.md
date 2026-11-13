# Configuración local de secretos

La aplicación importa obligatoriamente el archivo externo:

`%USERPROFILE%\.miapp\application-secrets.properties`

La ruta corresponde a `${user.home}/.miapp/application-secrets.properties`. Spring Boot no iniciará si el archivo falta o no se puede leer.

## Crear el archivo en Windows

1. Abre PowerShell con el mismo usuario que ejecutará la aplicación.
2. Crea la carpeta personal de configuración:

   ```powershell
   New-Item -ItemType Directory -Force "$HOME\.miapp"
   ```

3. Crea o abre el archivo:

   ```powershell
   notepad "$HOME\.miapp\application-secrets.properties"
   ```

4. Añade las propiedades siguientes, reemplazando cada marcador localmente. No compartas este archivo ni sus valores:

   ```properties
   DB_URL=jdbc:postgresql://localhost:5432/nombre_base
   DB_USERNAME=usuario_postgresql
   DB_PASSWORD=contraseña_postgresql
   GOOGLE_RECAPTCHA_SECRET=secreto_privado_recaptcha
   GOOGLE_RECAPTCHA_PUBLIC=clave_publica_recaptcha
   ```

   Usa una sola propiedad por línea, sin comillas. `DB_URL` debe ser una URL JDBC válida para tu base de datos.

5. Guarda el archivo. Debe permanecer en `%USERPROFILE%\.miapp`, fuera de la carpeta del proyecto.

## Comprobar que Spring Boot lo carga

Inicia la aplicación desde IntelliJ o mediante Maven, con el mismo usuario de Windows que creó el archivo. Si la importación se procesa, Spring continúa el arranque y utiliza la configuración de la base de datos. Si el archivo no existe o no es legible, el arranque falla indicando que no pudo cargar la ubicación importada.

No imprimas los valores del archivo en consola ni los registres en logs. La aplicación no muestra estos valores como parte de su configuración de inicio.

El archivo está fuera del proyecto y no forma parte de los recursos que Maven empaqueta en el JAR. La regla de `.gitignore` también evita agregar por accidente un archivo con ese nombre dentro del repositorio.
