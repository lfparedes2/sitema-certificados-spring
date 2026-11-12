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