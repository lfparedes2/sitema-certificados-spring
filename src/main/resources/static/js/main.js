/**
 * =========================================================================
 * SEGURIDAD CSRF
 * =========================================================================
 */
  document.addEventListener('DOMContentLoaded', function () {
      document.body.addEventListener('htmx:configRequest', function (event) {
          const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
          const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

          if (csrfToken && csrfHeader) {
              event.detail.headers[csrfHeader] = csrfToken;
          }
      });
  });

<!-- Script para el Menú Móvil -->
    function toggleMobileNav() {
        const drawer = document.getElementById('mobile-nav-drawer');
        const iconMenu = document.getElementById('icon-hamburger');
        const iconClose = document.getElementById('icon-close');

        drawer.classList.toggle('hidden');
        iconMenu.classList.toggle('hidden');
        iconClose.classList.toggle('hidden');
    }

    function toggleAccordion(button) {
        const content = button.nextElementSibling;
        const arrow = button.querySelector('.accordion-arrow');

        content.classList.toggle('hidden');
        arrow.classList.toggle('rotate-180');
        button.classList.toggle('bg-gray-100');
        button.classList.toggle('text-gray-900');
    }

<!-- Banner Condicional de Notificación de Errores -->
        /*<![CDATA[*/
        // Capturamos las variables de Thymeleaf de forma segura
        const errorStatus = /*[[${errorStatus}]]*/ null;
        const errorMessage = /*[[${errorMessage}]]*/ null;
        // Si existe un estado de error, disparamos SweetAlert2
        if (errorStatus) {
            Swal.fire({
                icon: 'error',
                title: errorStatus, // Muestra el "Código Error"
                text: errorMessage || 'Ocurrió un error inesperado.', // Muestra el mensaje de validación
                confirmButtonColor: '#ef4444', // Color rojo que combina con tu estilo anterior (Tailwind red-500)
                confirmButtonText: 'Entendido'
            });
        }
        /*]]>*/


        // Thymeleaf convierte el DTO de Java en un objeto JSON nativo de JavaScript de forma segura
       document.addEventListener('DOMContentLoaded', () => {
           const alertaEl = document.getElementById('alerta-data');

           if (alertaEl) {
               const tiempo = alertaEl.dataset.tiempo ? Number(alertaEl.dataset.tiempo) : null;

               Swal.fire({
                   icon: alertaEl.dataset.tipo,
                   title: alertaEl.dataset.titulo,
                   text: alertaEl.dataset.mensaje,
                   timer: tiempo,
                   timerProgressBar: Boolean(tiempo),
                   confirmButtonColor: alertaEl.dataset.color,
                   confirmButtonText: 'Entendido'
               });
           }
       });


        function mostrarAlerta() {
                    Swal.fire({
                        title: '¡Funciona!',
                        text: 'SweetAlert2 cargado de manera 100% manual y local.',
                        icon: 'success'
                    });
        }


        /**
         * Resetea por completo cualquier formulario o contenedor dado.
         * Limpia valores estándar, campos ocultos opcionales y mensajes de validación.
         *
         * @param {string|HTMLElement} target - Selector CSS (ej. '#formSolicitud') o elemento DOM (<form> o <dialog>).
         * @param {Object} opciones - Configuración opcional.
         * @param {boolean} opciones.limpiarOcultos - Si es true, también vacía los <input type="hidden"> (útil en modales de edición).
         */
        function resetearFormulario(target, opciones = { limpiarOcultos: false }) {
            const elemento = typeof target === 'string' ? document.querySelector(target) : target;
            if (!elemento) return;

            // Detecta si se pasó un <form> directamente o un contenedor (<dialog>, <div>) con uno o varios formularios
            const formularios = elemento.tagName === 'FORM'
                ? [elemento]
                : elemento.querySelectorAll('form');

            formularios.forEach((form) => {
                // 1. Reseteo nativo del navegador
                form.reset();

                // 2. Limpieza profunda (por si el formulario tenía valores precargados con th:value)
                form.querySelectorAll('input, textarea, select').forEach((campo) => {
                    if (campo.type === 'button' || campo.type === 'submit' || campo.type === 'reset') return;
                    if (campo.type === 'hidden' && !opciones.limpiarOcultos) return;

                    if (campo.type === 'checkbox' || campo.type === 'radio') {
                        campo.checked = false;
                    } else if (campo.tagName === 'SELECT') {
                        campo.selectedIndex = 0;
                    } else {
                        campo.value = '';
                    }
                });

                // 3. Limpia mensajes o clases de error personalizados si existen
                form.querySelectorAll('.error-mensaje, [data-error]').forEach((err) => {
                    err.textContent = '';
                });
            });
        }

        /**
         * Listener global automático:
         * Cualquier <dialog> que tenga el atributo `data-auto-reset="true"`
         * se limpiará automáticamente cada vez que se cierre (con botón, Esc o JS).
         */
        document.addEventListener('close', (evento) => {
            const dialog = evento.target;
            if (dialog && dialog.tagName === 'DIALOG' && dialog.dataset.autoReset === 'true') {
                const limpiarOcultos = dialog.dataset.resetHidden === 'true';
                resetearFormulario(dialog, { limpiarOcultos });
            }
        }, true); // El parámetro `true` (fase de captura) es obligatorio porque el evento 'close' de <dialog> no burbujea


// Reseteo automático al cerrar cualquier <dialog data-auto-reset="true">
document.addEventListener('close', (evento) => {
    const dialog = evento.target;
    if (dialog && dialog.tagName === 'DIALOG' && dialog.dataset.autoReset === 'true') {
        resetearFormulario(dialog, { limpiarOcultos: dialog.dataset.resetHidden === 'true' });
    }
}, true);

//timeout cierre de sesion
    document.addEventListener('DOMContentLoaded', function() {
        const timeoutAttr = document.body.dataset.timeout;
        const loginUrl = document.body.dataset.loginUrl || '/auth/login?timeout=true';

        console.log('Timeout detectado:', timeoutAttr, 'URL:', loginUrl); // <-- Para confirmar en consola

        if (!timeoutAttr) return;

        const TIEMPO_INACTIVIDAD = parseInt(timeoutAttr, 10);
        let temporizador;

        function cerrarSesionAutomaticamente() {
            console.log('Redirigiendo al login por inactividad...');
            window.location.href = loginUrl;
        }

        function reiniciarTemporizador() {
            clearTimeout(temporizador);
            temporizador = setTimeout(cerrarSesionAutomaticamente, TIEMPO_INACTIVIDAD);
        }

        reiniciarTemporizador();

        document.addEventListener('mousemove', reiniciarTemporizador);
        document.addEventListener('keydown', reiniciarTemporizador);
        document.addEventListener('click', reiniciarTemporizador);
        document.addEventListener('scroll', reiniciarTemporizador);
    });


/**
 * =========================================================================
 * MOTOR GLOBAL DE VALIDACIÓN INLINE Y RESETEO DE FORMULARIOS
 * Actívalo agregando `data-validar` a cualquier etiqueta <form>.
 * =========================================================================
 */

const MENSAJES_DEFECTO = {
    valueMissing: 'Este campo es obligatorio.',
    typeMismatch: {
        email: 'Ingresa un correo electrónico válido (ej. usuario@dominio.com).',
        url: 'Ingresa una dirección web válida (ej. https://ejemplo.com).',
        number: 'Ingresa un número válido.',
        tel: 'Ingresa un número de teléfono válido.'
    },
    tooShort: (min) => `Debe tener al menos ${min} caracteres.`,
    tooLong: (max) => `No puede superar los ${max} caracteres.`,
    rangeUnderflow: (min) => `El valor mínimo permitido es ${min}.`,
    rangeOverflow: (max) => `El valor máximo permitido es ${max}.`,
    stepMismatch: 'El valor ingresado no es válido.',
    patternMismatch: 'El formato ingresado no es válido.'
};

/**
 * Obtiene el mensaje de error exacto según la regla que falló
 */
function obtenerMensajeError(campo) {
    const v = campo.validity;
    if (v.valid) return '';

    // 1. Revisa si definiste un mensaje personalizado en el HTML (data-msg-*)
    if (v.valueMissing && campo.dataset.msgRequired) return campo.dataset.msgRequired;
    if (v.tooShort && campo.dataset.msgMinlength) return campo.dataset.msgMinlength;
    if (v.tooLong && campo.dataset.msgMaxlength) return campo.dataset.msgMaxlength;
    if (v.typeMismatch && campo.dataset.msgType) return campo.dataset.msgType;
    if (v.patternMismatch && campo.dataset.msgPattern) return campo.dataset.msgPattern;
    if (v.rangeUnderflow && campo.dataset.msgMin) return campo.dataset.msgMin;
    if (v.rangeOverflow && campo.dataset.msgMax) return campo.dataset.msgMax;

    // 2. Mensajes automáticos por defecto
    if (v.valueMissing) return MENSAJES_DEFECTO.valueMissing;
    if (v.typeMismatch) return MENSAJES_DEFECTO.typeMismatch[campo.type] || 'El formato no es válido.';
    if (v.tooShort) return MENSAJES_DEFECTO.tooShort(campo.minLength);
    if (v.tooLong) return MENSAJES_DEFECTO.tooLong(campo.maxLength);
    if (v.rangeUnderflow) return MENSAJES_DEFECTO.rangeUnderflow(campo.min);
    if (v.rangeOverflow) return MENSAJES_DEFECTO.rangeOverflow(campo.max);
    if (v.stepMismatch) return MENSAJES_DEFECTO.stepMismatch;
    if (v.patternMismatch) return MENSAJES_DEFECTO.patternMismatch;

    return 'Campo inválido.';
}

/**
 * Muestra u oculta el mensaje inline debajo de la caja y pinta el borde de rojo
 */
function validarCampo(campo) {
    if (campo.disabled || campo.type === 'hidden' || campo.type === 'submit' || campo.type === 'button') {
        return true;
    }

    // Evita espacios en blanco solos si el campo es requerido
    if (campo.required && typeof campo.value === 'string' && campo.value.trim() === '' && campo.value.length > 0) {
        campo.setCustomValidity('No se permiten solo espacios en blanco.');
    } else {
        campo.setCustomValidity('');
    }

    const contenedorGrupo = campo.closest('.group') || campo.parentElement;
    const cajaVisual = campo.closest('[data-caja]') || campo;

    // Busca o crea automáticamente el <p> de error debajo de la caja
    let msgEl = contenedorGrupo.querySelector('[data-error-msg]');
    if (!msgEl) {
        msgEl = document.createElement('p');
        msgEl.dataset.errorMsg = 'true';
        msgEl.className = 'mt-1.5 text-xs font-medium text-red-600 hidden flex items-center gap-1';
        contenedorGrupo.appendChild(msgEl);
    }

    const mensaje = campo.validationMessage && !campo.validity.valid
        ? (campo.validity.customError ? campo.validationMessage : obtenerMensajeError(campo))
        : '';

    if (mensaje) {
        msgEl.innerHTML = `<svg xmlns="http://w3.org" viewBox="0 0 20 20" fill="currentColor" class="w-3.5 h-3.5 shrink-0"><path fill-rule="evenodd" d="M18 10a8 8 0 1 1-16 0 8 8 0 0 1 16 0Zm-8-5a.75.75 0 0 1 .75.75v4.5a.75.75 0 0 1-1.5 0v-4.5A.75.75 0 0 1 10 5Zm0 10a1 1 0 1 0 0-2 1 1 0 0 0 0 2Z" clip-rule="evenodd" /></svg><span>${mensaje}</span>`;
        msgEl.classList.remove('hidden');
        cajaVisual.classList.add('border-red-500', 'ring-4', 'ring-red-500/10');
        cajaVisual.classList.remove('border-slate-300');
        return false;
    } else {
        msgEl.textContent = '';
        msgEl.classList.add('hidden');
        cajaVisual.classList.remove('border-red-500', 'ring-4', 'ring-red-500/10');
        cajaVisual.classList.add('border-slate-300');
        return true;
    }
}

/**
 * Inicializa automáticamente todos los formularios con `data-validar`
 */
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('form[data-validar]').forEach((form) => {
        // Desactiva las burbujas nativas del navegador
        form.setAttribute('novalidate', true);

        const campos = form.querySelectorAll('input, textarea, select');

        campos.forEach((campo) => {
            // Valida cuando el usuario sale del campo (blur)
            campo.addEventListener('blur', () => {
                campo.dataset.tocado = 'true';
                validarCampo(campo);
            });

            // Revalida en tiempo real mientras escribe si ya tuvo un error
            campo.addEventListener('input', () => {
                if (campo.dataset.tocado === 'true') {
                    validarCampo(campo);
                }
            });
        });

/*
        // Valida todo el formulario al intentar enviar
*/
        form.addEventListener('submit', (e) => {
            let formularioValido = true;
            let primerCampoInvalido = null;

            campos.forEach((campo) => {
                campo.dataset.tocado = 'true';
                const esValido = validarCampo(campo);
                if (!esValido) {
                    formularioValido = false;
                    if (!primerCampoInvalido) primerCampoInvalido = campo;
                }
            });

            if (!formularioValido) {
                e.preventDefault();
                primerCampoInvalido.focus();
            }
        });
    });
});

/**
 * Función genérica para resetear valores Y errores visuales de cualquier formulario
 */
function resetearFormulario(target, opciones = { limpiarOcultos: false }) {
    const elemento = typeof target === 'string' ? document.querySelector(target) : target;
    if (!elemento) return;

    const formularios = elemento.tagName === 'FORM' ? [elemento] : elemento.querySelectorAll('form');

    formularios.forEach((form) => {
        form.reset();

        form.querySelectorAll('input, textarea, select').forEach((campo) => {
            if (campo.type === 'button' || campo.type === 'submit' || campo.type === 'reset') return;
            if (campo.type === 'hidden' && !opciones.limpiarOcultos) return;

            delete campo.dataset.tocado;
            campo.setCustomValidity('');

            if (campo.type === 'checkbox' || campo.type === 'radio') {
                campo.checked = false;
            } else if (campo.tagName === 'SELECT') {
                campo.selectedIndex = 0;
            } else {
                campo.value = '';
            }

            // Limpia bordes rojos
            const cajaVisual = campo.closest('[data-caja]') || campo;
            cajaVisual.classList.remove('border-red-500', 'ring-4', 'ring-red-500/10');
            cajaVisual.classList.add('border-slate-300');
        });

        // Oculta todos los mensajes de error inline
        form.querySelectorAll('[data-error-msg]').forEach((msg) => {
            msg.textContent = '';
            msg.classList.add('hidden');
        });
    });
}


/**
 * =========================================================================
 * MOTOR GLOBAL DE VALIDACIÓN (CLIENTE + ARCHIVOS + ERRORES BACKEND SPRING BOOT)
 * Uso: Llamar a `iniciarMotorFormularios()` una vez en tu JS global.
 * =========================================================================
 */

 /**
  * Motor de validación de formularios en cliente con soporte para HTMX y Spring Boot.
  *
  * ### Tabla completa de opciones y atributos de uso
  *
  * | Categoría | Atributo / Elemento | Dónde se coloca | Función y comportamiento |
  * | :--- | :--- | :--- | :--- |
  * | **Activación y Modal** | `data-validar` | `<form>` | Activa el motor: desactiva burbujas nativas, valida en cliente y captura errores HTTP 400/422 de HTMX. |
  * | **Activación y Modal** | `data-modal-cerrar="#id"` | `<form>` | Selector CSS del modal que se oculta automáticamente solo si el backend responde HTTP 200 OK. |
  * | **Estructura Visual** | `class="group"` | `<div>` padre | Contenedor del campo donde el motor creará e insertará automáticamente el `<p data-error-msg>`. |
  * | **Estructura Visual** | `data-caja` | `<div>` o `<label>` | Elemento que recibirá el borde rojo (`border-red-500`). Ideal para envolver inputs file o personalizados. |
  * | **Estructura Visual** | `data-error-general` | `<div>` en `<form>` | Caja opcional donde se mostrará el campo `"mensajeGeneral"` devuelto por el JSON de Spring Boot. |
  * | **Reglas HTML5** | `required` | `<input>`, `<select>`, `<textarea>` | Valida que no esté vacío ni contenga solo espacios en blanco. |
  * | **Reglas HTML5** | `type="email / url / number / tel"` | `<input>` | Valida el formato correspondiente según el tipo de dato. |
  * | **Reglas HTML5** | `minlength` / `maxlength` | `<input>`, `<textarea>` | Valida la cantidad mínima y máxima de caracteres. |
  * | **Reglas HTML5** | `min` / `max` / `step` | `<input type="number">` | Valida rangos numéricos e incrementos permitidos. |
  * | **Reglas HTML5** | `pattern="regex"` | `<input>` | Valida el texto contra una expresión regular. |
  * | **Reglas Archivos** | `accept=".pdf,image/*"` | `<input type="file">` | Valida extensión exacta (`.pdf`) o tipo MIME (`image/*`, `application/pdf`). |
  * | **Reglas Archivos** | `data-max-mb="5"` | `<input type="file">` | Valida el peso máximo permitido en Megabytes por archivo (admite decimales como `1.5`). |
  * | **Reglas Archivos** | `data-max-files="3"` | `<input type="file" multiple>` | Valida la cantidad máxima de archivos seleccionados simultáneamente. |
  * | **Mensajes Custom** | `data-msg-required` | Cualquier campo | Mensaje personalizado cuando falla `required`. |
  * | **Mensajes Custom** | `data-msg-type` | `<input>` | Mensaje personalizado cuando falla `email`, `url`, `number` o `tel`. |
  * | **Mensajes Custom** | `data-msg-minlength` / `maxlength` | `<input>`, `<textarea>` | Mensaje personalizado cuando falla la longitud de texto. |
  * | **Mensajes Custom** | `data-msg-min` / `max` | `<input type="number">` | Mensaje personalizado cuando falla el rango numérico. |
  * | **Mensajes Custom** | `data-msg-pattern` | `<input>` | Mensaje personalizado cuando falla el atributo `pattern`. |
  * | **Mensajes Custom** | `data-msg-max-mb` | `<input type="file">` | Mensaje personalizado cuando el archivo supera el peso de `data-max-mb`. |
  * | **Mensajes Custom** | `data-msg-accept` | `<input type="file">` | Mensaje personalizado cuando la extensión o MIME no coincide con `accept`. |
  * | **Mensajes Custom** | `data-msg-max-files` | `<input type="file">` | Mensaje personalizado cuando se sube una cantidad mayor a `data-max-files`. |
  * | **Función Global** | `resetearFormulario(selector, opts)` | Llamada JS / `onclick` | Limpia valores, archivos, bordes rojos y mensajes. Opcional: `{ limpiarOcultos: true }` para borrar `type="hidden"`. |
  *
  * @param {string | HTMLFormElement} selector - Selector CSS o referencia directa al elemento `<form>`.
  * @param {Object} [opts] - Opciones adicionales de configuración.
  * @param {boolean} [opts.limpiarOcultos=false] - Si es `true`, también limpia los campos con `type="hidden"`.
  * @returns {void}
  */
/**
 * =========================================================================
 * MOTOR GLOBAL DE VALIDACIÓN SEGURO (OWASP XSS-SAFE + HTMX + <dialog>)
 * =========================================================================
 */
function iniciarMotorFormularios() {
    if (window.__motorFormulariosActivo) return;
    window.__motorFormulariosActivo = true;

    const MENSAJES_DEFECTO = {
        valueMissing: 'Este campo es obligatorio.',
        typeMismatch: {
            email: 'Ingresa un correo electrónico válido (ej. usuario@dominio.com).',
            url: 'Ingresa una dirección web válida (ej. https://ejemplo.com).',
            number: 'Ingresa un número válido.',
            tel: 'Ingresa un número de teléfono válido.'
        },
        tooShort: (min) => `Debe tener al menos ${min} caracteres.`,
        tooLong: (max) => `No puede superar los ${max} caracteres.`,
        rangeUnderflow: (min) => `El valor mínimo permitido es ${min}.`,
        rangeOverflow: (max) => `El valor máximo permitido es ${max}.`,
        stepMismatch: 'El valor ingresado no es válido.',
        patternMismatch: 'El formato ingresado no es válido.',
        fileTooBig: (mb) => `El archivo supera el tamaño máximo permitido de ${mb} MB.`,
        fileTypeMismatch: (tipos) => `Formato no válido. Solo se permite: ${tipos}.`,
        fileMaxCount: (max) => `Solo puedes subir un máximo de ${max} archivo(s).`
    };

    const ICONO_ERROR = '<svg xmlns="http://w3.org" viewBox="0 0 20 20" fill="currentColor" class="w-3.5 h-3.5 shrink-0"><path fill-rule="evenodd" d="M18 10a8 8 0 1 1-16 0 8 8 0 0 1 16 0Zm-8-5a.75.75 0 0 1 .75.75v4.5a.75.75 0 0 1-1.5 0v-4.5A.75.75 0 0 1 10 5Zm0 10a1 1 0 1 0 0-2 1 1 0 0 0 0 2Z" clip-rule="evenodd" /></svg>';

    // Función interna segura contra XSS (OWASP A03)
    function renderizarMensajeSeguro(contenedor, textoMensaje) {
        contenedor.innerHTML = ICONO_ERROR;
        const spanTexto = document.createElement('span');
        spanTexto.textContent = textoMensaje;
        contenedor.appendChild(spanTexto);
    }

    function aplicarEstadoVisual(campo, mensaje) {
        const contenedorGrupo = campo.closest('.group') || campo.parentElement;
        const cajaVisual = campo.closest('[data-caja]') || campo;

        let msgEl = contenedorGrupo.querySelector('[data-error-msg]');
        if (!msgEl) {
            msgEl = document.createElement('p');
            msgEl.dataset.errorMsg = 'true';
            msgEl.className = 'mt-1.5 text-xs font-medium text-red-600 hidden flex items-center gap-1';
            contenedorGrupo.appendChild(msgEl);
        }

        if (mensaje) {
            renderizarMensajeSeguro(msgEl, mensaje);
            msgEl.classList.remove('hidden');
            cajaVisual.classList.add('border-red-500', 'ring-4', 'ring-red-500/10');
            cajaVisual.classList.remove('border-slate-300');
            return false;
        } else {
            msgEl.textContent = '';
            msgEl.classList.add('hidden');
            cajaVisual.classList.remove('border-red-500', 'ring-4', 'ring-red-500/10');
            cajaVisual.classList.add('border-slate-300');
            return true;
        }
    }

    function validarArchivo(campo) {
        const archivos = campo.files;
        if (!archivos || archivos.length === 0) return '';

        const maxFiles = parseInt(campo.dataset.maxFiles, 10);
        if (maxFiles && archivos.length > maxFiles) {
            return campo.dataset.msgMaxFiles || MENSAJES_DEFECTO.fileMaxCount(maxFiles);
        }

        const maxMb = parseFloat(campo.dataset.maxMb);
        const maxBytes = maxMb ? maxMb * 1024 * 1024 : null;
        const acceptAttr = campo.getAttribute('accept');
        const tiposPermitidos = acceptAttr ? acceptAttr.split(',').map(t => t.trim().toLowerCase()) : [];

        for (let i = 0; i < archivos.length; i++) {
            const archivo = archivos[i];
            if (maxBytes && archivo.size > maxBytes) {
                return campo.dataset.msgMaxMb || MENSAJES_DEFECTO.fileTooBig(maxMb);
            }
            if (tiposPermitidos.length > 0) {
                const nombre = archivo.name.toLowerCase();
                const mime = archivo.type.toLowerCase();
                const valido = tiposPermitidos.some(tipo => {
                    if (tipo.startsWith('.')) return nombre.endsWith(tipo);
                    if (tipo.endsWith('/*')) return mime.startsWith(tipo.replace('/*', '/'));
                    return mime === tipo;
                });
                if (!valido) return campo.dataset.msgAccept || MENSAJES_DEFECTO.fileTypeMismatch(acceptAttr);
            }
        }
        return '';
    }

    function obtenerMensajeError(campo) {
        const v = campo.validity;
        if (v.valid) return '';

        if (v.valueMissing && campo.dataset.msgRequired) return campo.dataset.msgRequired;
        if (v.tooShort && campo.dataset.msgMinlength) return campo.dataset.msgMinlength;
        if (v.tooLong && campo.dataset.msgMaxlength) return campo.dataset.msgMaxlength;
        if (v.typeMismatch && campo.dataset.msgType) return campo.dataset.msgType;
        if (v.patternMismatch && campo.dataset.msgPattern) return campo.dataset.msgPattern;
        if (v.rangeUnderflow && campo.dataset.msgMin) return campo.dataset.msgMin;
        if (v.rangeOverflow && campo.dataset.msgMax) return campo.dataset.msgMax;

        if (v.valueMissing) return MENSAJES_DEFECTO.valueMissing;
        if (v.typeMismatch) return MENSAJES_DEFECTO.typeMismatch[campo.type] || 'El formato no es válido.';
        if (v.tooShort) return MENSAJES_DEFECTO.tooShort(campo.minLength);
        if (v.tooLong) return MENSAJES_DEFECTO.tooLong(campo.maxLength);
        if (v.rangeUnderflow) return MENSAJES_DEFECTO.rangeUnderflow(campo.min);
        if (v.rangeOverflow) return MENSAJES_DEFECTO.rangeOverflow(campo.max);
        if (v.stepMismatch) return MENSAJES_DEFECTO.stepMismatch;
        if (v.patternMismatch) return MENSAJES_DEFECTO.patternMismatch;

        return 'Campo inválido.';
    }

    function validarCampo(campo) {
        if (!campo || campo.disabled || ['hidden', 'submit', 'button', 'reset'].includes(campo.type)) return true;

        campo.setCustomValidity('');
        delete campo.dataset.errorBackend;

        if (campo.required && campo.type !== 'file' && typeof campo.value === 'string' && campo.value.trim() === '' && campo.value.length > 0) {
            campo.setCustomValidity('No se permiten solo espacios en blanco.');
        }

        if (campo.type === 'file') {
            const errorArchivo = validarArchivo(campo);
            if (errorArchivo) campo.setCustomValidity(errorArchivo);
        }

        const mensaje = !campo.validity.valid
            ? (campo.validity.customError ? campo.validationMessage : obtenerMensajeError(campo))
            : '';

        return aplicarEstadoVisual(campo, mensaje);
    }

    function validarFormularioCompleto(form) {
        form.setAttribute('novalidate', true);

        const alertaGeneral = form.querySelector('[data-error-general]');
        if (alertaGeneral) {
            alertaGeneral.classList.add('hidden');
            alertaGeneral.textContent = '';
        }

        let formularioValido = true;
        let primerCampoInvalido = null;

        form.querySelectorAll('input, textarea, select').forEach((campo) => {
            campo.dataset.tocado = 'true';
            if (!validarCampo(campo)) {
                formularioValido = false;
                if (!primerCampoInvalido) primerCampoInvalido = campo;
            }
        });

        if (!formularioValido && primerCampoInvalido) primerCampoInvalido.focus();
        return formularioValido;
    }

    function mostrarErroresBackend(form, respuestaJson) {
        let primerCampoError = null;
        const mapaErrores = respuestaJson.errores || respuestaJson.errors || respuestaJson;

        if (mapaErrores && typeof mapaErrores === 'object') {
            Object.entries(mapaErrores).forEach(([nombreCampo, mensajeError]) => {
                if (nombreCampo === 'mensajeGeneral' || nombreCampo === 'message') return;

                const campo = form.querySelector('[name="' + nombreCampo + '"]');
                if (campo) {
                    campo.dataset.tocado = 'true';
                    campo.dataset.errorBackend = 'true';
                    campo.setCustomValidity(mensajeError);
                    aplicarEstadoVisual(campo, mensajeError);
                    if (!primerCampoError) primerCampoError = campo;
                }
            });
        }

        const msgGeneral = respuestaJson.mensajeGeneral || respuestaJson.message;
        const alertaGeneral = form.querySelector('[data-error-general]');
        if (msgGeneral && alertaGeneral) {
            renderizarMensajeSeguro(alertaGeneral, msgGeneral);
            alertaGeneral.classList.remove('hidden');
        }

        if (primerCampoError) primerCampoError.focus();
    }

    document.addEventListener('focusout', (e) => {
        const campo = e.target;
        const form = campo.closest && campo.closest('form[data-validar]');
        if (!form) return;

        form.setAttribute('novalidate', true);
        if (['INPUT', 'TEXTAREA', 'SELECT'].includes(campo.tagName)) {
            campo.dataset.tocado = 'true';
            validarCampo(campo);
        }
    });

    document.addEventListener('change', (e) => {
        const campo = e.target;
        const form = campo.closest && campo.closest('form[data-validar]');
        if (!form) return;

        if (campo.type === 'file' || campo.tagName === 'SELECT') {
            campo.dataset.tocado = 'true';
            validarCampo(campo);
        }
    });

    document.addEventListener('input', (e) => {
        const campo = e.target;
        const form = campo.closest && campo.closest('form[data-validar]');
        if (!form) return;
    if (campo.dataset.tocado === 'true' || campo.dataset.errorBackend === 'true') {
    validarCampo(campo);
    }
    });
    document.addEventListener('submit', (e) => {
    const form = e.target.closest && e.target.closest('form[data-validar]');
    if (!form) return;
    if (!validarFormularioCompleto(form)) {
    e.preventDefault();
    e.stopImmediatePropagation();
    }
    }, true);
    document.addEventListener('htmx:beforeRequest', (e) => {
    const elt = e.detail.elt;
    const form = elt.tagName === 'FORM' ? elt : elt.closest('form[data-validar]');
    if (!form || !form.hasAttribute('data-validar')) return;
    if (!validarFormularioCompleto(form)) {
    e.preventDefault();
    }
    });
    document.addEventListener('htmx:beforeSwap', (e) => {
    const elt = e.detail.elt;
    const form = elt.tagName === 'FORM' ? elt : elt.closest('form[data-validar]');
    if (!form || !form.hasAttribute('data-validar')) return;
    const status = e.detail.xhr.status;
    if (status === 400 || status === 422) {
    e.detail.shouldSwap = false;
    try {
    const data = JSON.parse(e.detail.xhr.responseText);
    mostrarErroresBackend(form, data);
    } catch (err) {
    console.error('Error parseando JSON del backend:', err);
    }
    }
    });
    document.addEventListener('htmx:afterRequest', (e) => {
    const elt = e.detail.elt;
    const form = elt.tagName === 'FORM' ? elt : elt.closest('form[data-validar]');
    if (!form || !form.hasAttribute('data-validar')) return;
    if (e.detail.successful) {
    window.resetearFormulario(form);
    const selectorModal = form.dataset.modalCerrar;
    if (selectorModal) {
    const modal = document.querySelector(selectorModal);
    if (modal) {
    if (modal.tagName === 'DIALOG' && typeof modal.close === 'function') {
    modal.close();
    } else {
    modal.classList.add('hidden');
    }
    }
    }
    }
    });
    window.resetearFormulario = function(target, opciones = { limpiarOcultos: false }) {
    const elemento = typeof target === 'string' ? document.querySelector(target) : target;
    if (!elemento) return;
    const formularios = elemento.tagName === 'FORM' ? [elemento] : elemento.querySelectorAll('form');
    formularios.forEach((form) => {
    form.reset();
    const alertaGeneral = form.querySelector('[data-error-general]');
    if (alertaGeneral) {
    alertaGeneral.classList.add('hidden');
    alertaGeneral.textContent = '';
    }
    form.querySelectorAll('input, textarea, select').forEach((campo) => {
    if (['button', 'submit', 'reset'].includes(campo.type)) return;
    if (campo.type === 'hidden' && !opciones.limpiarOcultos) return;
    delete campo.dataset.tocado;
    delete campo.dataset.errorBackend;
    campo.setCustomValidity('');
    if (campo.type === 'checkbox' || campo.type === 'radio') campo.checked = false;
    else if (campo.tagName === 'SELECT') campo.selectedIndex = 0;
    else campo.value = '';
    aplicarEstadoVisual(campo, '');
    });
    });
    };
    }
    iniciarMotorFormularios();

 /**
  * Efecto visual de sacudida (shake).
  * Funciona al hacer clic en cualquier botón submit si el formulario no pasa la validación.
  */
 function sacudirElemento(form) {
     if (!form) return;

     // 1. Busca el contenedor ideal: <dialog>, <main>, la tarjeta con sombra (.shadow-2xl) o el mismo <form>
     const contenedor = form.closest('dialog')
         || form.closest('main')
         || form.closest('.shadow-2xl')
         || form;

     // 2. Inyecta el CSS de la animación una sola vez (con !important para ganar sobre Animate.css)
     if (!document.getElementById('estilo-shake-validacion')) {
         const style = document.createElement('style');
         style.id = 'estilo-shake-validacion';
         style.textContent = `
             @keyframes sacudidaError {
                 0%, 100% { transform: translateX(0); }
                 20%, 60% { transform: translateX(-8px); }
                 40%, 80% { transform: translateX(8px); }
             }
             .animacion-shake {
                 animation: sacudidaError 0.35s ease-in-out !important;
             }
         `;
         document.head.appendChild(style);
     }

     // 3. Quita clases de Animate.css si existen (como animate__zoomIn) para que no bloqueen el transform
     contenedor.classList.remove('animate__zoomIn', 'animate__fadeIn', 'animacion-shake');
     void contenedor.offsetWidth; // Reinicia animación
     contenedor.classList.add('animacion-shake');

     contenedor.addEventListener('animationend', () => {
         contenedor.classList.remove('animacion-shake');
     }, { once: true });
 }

 // A) Se dispara al hacer CLIC en el botón Enviar/Guardar/Registrar
 document.addEventListener('click', (e) => {
     const btn = e.target.closest('button[type="submit"], input[type="submit"]');
     if (!btn) return;

     const form = btn.closest('form[data-validar]');
     if (!form) return;

     // Espera 10ms a que todas las validaciones (campos, email, reCAPTCHA) terminen de ejecutarse
     setTimeout(() => {
         const tieneErroresVisuales = form.querySelector('[data-error-msg]:not(.hidden), [data-client-error]:not(.hidden), #recaptcha-error:not(.hidden)');
         const captchaVisible = document.getElementById('recaptcha-error')?.style.display === 'block';
         const formInvalido = !form.checkValidity();

         if (e.defaultPrevented || tieneErroresVisuales || captchaVisible || formInvalido) {
             sacudirElemento(form);
         }
     }, 10);
 });

 // B) También se dispara si el usuario presiona ENTER dentro de un input
 document.addEventListener('submit', (e) => {
     const form = e.target;
     if (!form || !form.matches('form[data-validar]')) return;

     setTimeout(() => {
         if (e.defaultPrevented || !form.checkValidity()) {
             sacudirElemento(form);
         }
     }, 10);
 });

 // C) Si Spring Boot devolvió errores tras recargar la página o hacer swap con HTMX
 document.addEventListener('DOMContentLoaded', () => {
     document.querySelectorAll('form[data-validar]').forEach((form) => {
         if (form.querySelector('[data-server-error]')) {
             sacudirElemento(form);
         }
     });
 });

function sacudirElemento(form) {
    if (!form) return;

    // 1. Inyecta el CSS de la animación una sola vez
    if (!document.getElementById('estilo-shake-validacion')) {
        const style = document.createElement('style');
        style.id = 'estilo-shake-validacion';
        style.textContent = `
            @keyframes sacudidaError {
                0%, 100% { transform: translateX(0); }
                20%, 60% { transform: translateX(-7px); }
                40%, 80% { transform: translateX(7px); }
            }
            .animacion-shake {
                animation: sacudidaError 0.35s ease-in-out !important;
            }
        `;
        document.head.appendChild(style);
    }

    // 2. Evita que aparezca scroll horizontal en el modal padre durante la sacudida
    const dialogPadre = form.closest('dialog');
    if (dialogPadre) {
        dialogPadre.style.overflowX = 'hidden';
    }

    // 3. Aplica la sacudida directamente al <form>
    form.classList.remove('animate__zoomIn', 'animate__fadeIn', 'animacion-shake');
    void form.offsetWidth; // Reinicia la animación en cada clic
    form.classList.add('animacion-shake');

    form.addEventListener('animationend', () => {
        form.classList.remove('animacion-shake');
    }, { once: true });
}

// A) Se dispara al hacer CLIC en el botón Enviar/Guardar/Registrar
document.addEventListener('click', (e) => {
    const btn = e.target.closest('button[type="submit"], input[type="submit"]');
    if (!btn) return;

    const form = btn.closest('form[data-validar]');
    if (!form) return;

    setTimeout(() => {
        const tieneErroresVisuales = form.querySelector('[data-error-msg]:not(.hidden), [data-client-error]:not(.hidden), #recaptcha-error:not(.hidden)');
        const captchaVisible = form.querySelector('#recaptcha-error')?.style.display === 'block';
        const formInvalido = !form.checkValidity();

        if (e.defaultPrevented || tieneErroresVisuales || captchaVisible || formInvalido) {
            sacudirElemento(form);
        }
    }, 10);
});

// B) También se dispara si el usuario presiona ENTER dentro de un input
document.addEventListener('submit', (e) => {
    const form = e.target;
    if (!form || !form.matches('form[data-validar]')) return;

    setTimeout(() => {
        if (e.defaultPrevented || !form.checkValidity()) {
            sacudirElemento(form);
        }
    }, 10);
});

// C) Si Spring Boot devolvió errores tras recargar la página
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('form[data-validar]').forEach((form) => {
        if (form.querySelector('[data-server-error]')) {
            sacudirElemento(form);
        }
    });
});

// D) Si Spring Boot devolvió errores tras un intercambio con HTMX
document.addEventListener('htmx:afterSwap', (e) => {
    const target = e.detail.target;
    const form = (target.matches && target.matches('form[data-validar]'))
        ? target
        : target.querySelector('form[data-validar]');

    if (form && form.querySelector('[data-server-error]')) {
        sacudirElemento(form);
    }
});


/*
====================================================================================================================================================================
                                              TABLA COMPLETA DE OPCIONES Y ATRlBUTOS DE USO
====================================================================================================================================================================

| Categoría          | Atributo / Elemento              | Dónde se coloca               | Función y comportamiento                                                 |
|--------------------|----------------------------------|-------------------------------|--------------------------------------------------------------------------|
| Activación y Modal | data-validar                     | <form>                        | Activa el motor: desactiva burbujas nativas, valida en cliente y captura |
|                    |                                  |                               | errores HTTP 400/422 de HTMX.                                            |
| Activación y Modal | data-modal-cerrar="#id"          | <form>                        | Selector CSS del modal que se oculta automáticamente solo si el backend  |
|                    |                                  |                               | responde HTTP 200 OK.                                                    |
|--------------------|----------------------------------|-------------------------------|--------------------------------------------------------------------------|
| Estructura Visual  | class="group"                    | <div> padre                   | Contenedor del campo donde el motor creará e insertará automáticamente   |
|                    |                                  |                               | el <p data-error-msg>.                                                   |
| Estructura Visual  | data-caja                        | <div> o <label>               | Elemento que recibirá el borde rojo (border-red-500). Ideal para         |
|                    |                                  |                               | envolver inputs file o personalizados.                                   |
| Estructura Visual  | data-error-general               | <div> en <form>               | Caja opcional donde se mostrará el campo "mensajeGeneral" devuelto por   |
|                    |                                  |                               | el JSON de Spring Boot.                                                  |
|--------------------|----------------------------------|-------------------------------|--------------------------------------------------------------------------|
| Reglas HTML5       | required                         | <input>, <select>, <textarea> | Valida que no esté vacío ni contenga solo espacios en blanco.            |
| Reglas HTML5       | type="email / url / number / tel"| <input>                       | Valida el formato correspondiente según el tipo de dato.                 |
| Reglas HTML5       | minlength / maxlength            | <input>, <textarea>           | Valida la cantidad mínima y máxima de caracteres.                        |
| Reglas HTML5       | min / max / step                 | <input type="number">         | Valida rangos numéricos e incrementos permitidos.                        |
| Reglas HTML5       | pattern="regex"                  | <input>                       | Valida el texto contra una expresión regular.                            |
|--------------------|----------------------------------|-------------------------------|--------------------------------------------------------------------------|
| Reglas Archivos    | accept=".pdf,image/*"            | <input type="file">           | Valida extensión exacta (.pdf) o tipo MIME (image/*, application/pdf).   |
| Reglas Archivos    | data-max-mb="5"                  | <input type="file">           | Valida el peso máximo permitido en Megabytes por archivo (admite         |
|                    |                                  |                               | decimales como 1.5).                                                     |
| Reglas Archivos    | data-max-files="3"               | <input type="file" multiple>  | Valida la cantidad máxima de archivos seleccionados simultáneamente.     |
|--------------------|----------------------------------|-------------------------------|--------------------------------------------------------------------------|
| Mensajes Custom    | data-msg-required                | Cualquier campo               | Mensaje personalizado cuando falla required.                             |
| Mensajes Custom    | data-msg-type                    | <input>                       | Mensaje personalizado cuando falla email, url, number o tel.             |
| Mensajes Custom    | data-msg-minlength / maxlength   | <input>, <textarea>           | Mensaje personalizado cuando falla la longitud de texto.                 |
| Mensajes Custom    | data-msg-min / max               | <input type="number">         | Mensaje personalizado cuando falla el rango numérico.                    |
| Mensajes Custom    | data-msg-pattern                 | <input>                       | Mensaje personalizado cuando falla el atributo pattern.                  |
| Mensajes Custom    | data-msg-max-mb                  | <input type="file">           | Mensaje personalizado cuando el archivo supera el peso de data-max-mb.   |
| Mensajes Custom    | data-msg-accept                  | <input type="file">           | Mensaje personalizado cuando la extensión o MIME no coincide con accept. |
| Mensajes Custom    | data-msg-max-files               | <input type="file">           | Mensaje personalizado cuando se sube una cantidad mayor a data-max-files.|
|--------------------|----------------------------------|-------------------------------|--------------------------------------------------------------------------|
| Función Global     | resetearFormulario(sel, opts)    | Llamada JS / onclick          | Limpia valores, archivos, bordes rojos y mensajes.                       |
|                    |                                  |                               | Opcional: { limpiarOcultos: true } para borrar type="hidden".            |
====================================================================================================================================================================
*/

/**
 * =========================================================================
 * FIN MOTOR GLOBAL DE VALIDACIÓN (CLIENTE + ARCHIVOS + ERRORES BACKEND SPRING BOOT)
 * =========================================================================
 */

 /*******************************
    // Proceso del loader interno
    ********************************/
     document.addEventListener('htmx:afterSwap', function() {
         if (typeof initFlowbite === 'function') {
             initFlowbite();
         }
     });