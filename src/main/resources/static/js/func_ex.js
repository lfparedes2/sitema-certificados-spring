document.addEventListener("DOMContentLoaded", () => {
    // === 1. LÓGICA DEL MENU HAMBURGUESA ===
    const menuBtn = document.getElementById('menu-btn');
    const menuList = document.getElementById('menu-list');

    if (menuBtn && menuList) {
        // Alternar el menú al hacer clic en el botón hamburguesa
        menuBtn.addEventListener('click', () => {
            menuList.classList.toggle('hidden');
            menuList.classList.toggle('flex');
        });

        // Seleccionar todos los inputs del formulario
        const inputs = document.querySelectorAll('#loginForm input');

        // Ocultar el menú cuando el mouse entra a cualquier input
        inputs.forEach(input => {
            input.addEventListener('mouseenter', () => {
                menuList.classList.add('hidden');
                menuList.classList.remove('flex');
            });
        });
    }

    // === 2. LÓGICA DEL LOGIN (SOLO SI EXISTE EL FORMULARIO) ===
    const form = document.getElementById('loginForm');

    if (form) {
        const passwordInput = document.getElementById('password');
        const togglePasswordButton = document.getElementById('togglePassword');
        const eyeOpen = document.getElementById('eye-open');
        const eyeClosed = document.getElementById('eye-closed');
        const usernameInput = document.getElementById('username');
        const usernameError = document.getElementById('username-error');
        const passwordError = document.getElementById('password-error');
        const recaptchaError = document.getElementById('recaptcha-error');

        // Visibilidad de contraseña (Alternar ojo)
        if (togglePasswordButton) {
            togglePasswordButton.addEventListener('click', () => {
                const isPassword = passwordInput.type === 'password';
                passwordInput.type = isPassword ? 'text' : 'password';
                eyeOpen.classList.toggle('hidden', isPassword);
                eyeClosed.classList.toggle('hidden', !isPassword);
            });
        }

        // Función de validación visual de longitud mínima
        function validateField(input, errorElement, minLength, fieldName) {
            if (!input || !errorElement) return false;
            if (input.value.length === 0) {
                errorElement.textContent = "Este campo es requerido.";
                errorElement.classList.remove('hidden');
                input.classList.add('border-red-500', 'focus:ring-red-500/10', 'focus:border-red-500');
                return false;
            } else if (input.value.length < minLength) {
                errorElement.textContent = `El ${fieldName} debe tener al menos ${minLength} caracteres (llevas ${input.value.length}).`;
                errorElement.classList.remove('hidden');
                input.classList.add('border-red-500', 'focus:ring-red-500/10', 'focus:border-red-500');
                return false;
            } else {
                errorElement.classList.add('hidden');
                input.classList.remove('border-red-500', 'focus:ring-red-500/10', 'focus:border-red-500');
                return true;
            }
        }

        // Escuchar cambios en los inputs después de escribir
        if (usernameInput) usernameInput.addEventListener('input', () => validateField(usernameInput, usernameError, 5, 'usuario'));
        if (passwordInput) passwordInput.addEventListener('input', () => validateField(passwordInput, passwordError, 8, 'contraseña'));

        // Interceptar evento Submit del formulario
        form.addEventListener('submit', (e) => {
            const isUsernameValid = validateField(usernameInput, usernameError, 5, 'usuario');
            const isPasswordValid = validateField(passwordInput, passwordError, 8, 'contraseña');

            // Comprobar si el usuario resolvió el captcha de Google
            let isRecaptchaValid = true;
            if (typeof grecaptcha !== 'undefined') {
                const recaptchaResponse = grecaptcha.getResponse();
                if (!recaptchaResponse) {
                    if (recaptchaError) recaptchaError.classList.remove('hidden');
                    isRecaptchaValid = false;
                } else {
                    if (recaptchaError) recaptchaError.classList.add('hidden');
                }
            }

            // Si alguna regla no se cumple, bloquear petición POST
            if (!isUsernameValid || !isPasswordValid || !isRecaptchaValid) {
                e.preventDefault();
            }
        });
    }
});
