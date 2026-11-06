package com.experto.seguridad.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Map;

public class RecaptchaFilter extends OncePerRequestFilter {

    // REEMPLAZA ESTO con tu clave secreta de Google reCAPTCHA (Empieza por 6L...)
    private static final String RECAPTCHA_SECRET = "6LfclFwUAAAAAAX57E87Hl2tTyeywHy97462fM1W";
    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Solo interceptar si es la petición POST del login
        if ("/auth/login".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) {
            String recaptchaResponse = request.getParameter("g-recaptcha-response");

            if (recaptchaResponse == null || !verifyRecaptcha(recaptchaResponse)) {
                System.out.println("[DIAGNÓSTICO ERRORES] reCAPTCHA no válido o vacío.");
                // Redirigir de vuelta al login agregando el parámetro de error para Thymeleaf
                response.sendRedirect("/auth/login?error=true");
                return; // Corta el flujo, no permite continuar hacia el proceso de login
            }
            System.out.println("[DIAGNÓSTICO] reCAPTCHA verificado con éxito del lado del servidor.");
        }

        // Si pasa la validación o no es una petición de login, continúa el flujo normal
        filterChain.doFilter(request, response);
    }

    private boolean verifyRecaptcha(String recaptchaResponse) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // 1. Configurar los encabezados (Headers) para enviar datos de formulario
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // 2. Configurar los parámetros en el cuerpo (Body) de la petición como exige Google
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("secret", RECAPTCHA_SECRET);
            map.add("response", recaptchaResponse);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

            // 3. Realizar la petición POST de forma segura
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                    RECAPTCHA_VERIFY_URL,
                    requestEntity,
                    Map.class
            );

            // 4. Evaluar la respuesta en formato JSON estructurado
            Map<String, Object> apiResponse = responseEntity.getBody();
            if (apiResponse != null) {
                return (Boolean) apiResponse.get("success");
            }
        } catch (Exception e) {
            System.err.println("[DIAGNÓSTICO ERRORES] Error conectando con el servidor de Google reCAPTCHA: " + e.getMessage());
        }
        return false;
    }
}
