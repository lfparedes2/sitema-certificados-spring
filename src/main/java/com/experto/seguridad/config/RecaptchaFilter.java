package com.experto.seguridad.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class RecaptchaFilter extends OncePerRequestFilter {

    @Value("${google.recaptcha.secret}")
    private String recaptchaSecret;

    @Value("${google.recaptcha.verify.url}")
    private String recaptchaVerifyUrl;

    // Rutas protegidas por reCAPTCHA en peticiones POST
    private static final Set<String> RUTAS_PROTEGIDAS = Set.of("/auth/login", "/auth/register");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        if ("POST".equalsIgnoreCase(request.getMethod()) && RUTAS_PROTEGIDAS.contains(uri)) {
            String recaptchaResponse = request.getParameter("g-recaptcha-response");

            if (recaptchaResponse == null || recaptchaResponse.trim().isEmpty() || !verifyRecaptcha(recaptchaResponse, request)) {
                log.warn("Validación reCAPTCHA fallida en la ruta: {}", uri);

                // Para Login mantiene tu redirección original
                if ("/auth/login".equals(uri)) {
                    response.sendRedirect("/auth/login?recaptchaError=true");
                    return;
                }

                // Para Registro marca el error en el request para no perder los datos escritos por el usuario
                request.setAttribute("recaptchaInvalido", "Por favor, verifica la casilla 'No soy un robot' antes de continuar.");
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean verifyRecaptcha(String recaptchaResponse, HttpServletRequest request) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("secret", recaptchaSecret);
            map.add("response", recaptchaResponse);
            map.add("remoteip", obtenerIpCliente(request));

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                    recaptchaVerifyUrl,
                    requestEntity,
                    Map.class
            );

            Map<String, Object> apiResponse = responseEntity.getBody();
            if (apiResponse != null && apiResponse.containsKey("success")) {
                return Boolean.TRUE.equals(apiResponse.get("success"));
            }
        } catch (Exception e) {
            log.error("Error conectando con Google reCAPTCHA: {}", e.getMessage());
        }
        return false;
    }

    private String obtenerIpCliente(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
