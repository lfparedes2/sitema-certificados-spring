package com.experto.seguridad.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;

    // Inyectamos el nuevo manejador de éxito
    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            RecaptchaFilter recaptchaFilter
    ) throws Exception {
        http
                .headers(headers -> headers
                        // Evita que tu app se cargue dentro de un <iframe> en sitios externos (Anti-Clickjacking)
                        .frameOptions(frame -> frame.deny())
                        // Fuerza el uso de HTTPS en el navegador durante 1 año (HSTS)
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000)
                        )
                        // Controla qué información de URL se envía al navegar hacia enlaces externos
                        .referrerPolicy(referrer -> referrer
                                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                        )
                        .addHeaderWriter(new StaticHeadersWriter("Permissions-Policy", "geolocation=(), microphone=(), camera=()"))
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/css/**", "/js/**", "/images/**", "/assets/**", "/public/**", "/vite.svg").permitAll()
                        .requestMatchers("/dashboard/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(recaptchaFilter, UsernamePasswordAuthenticationFilter.class)

                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        // Usamos el manejador inyectado
                        .successHandler(successHandler)
                        .failureHandler(customAuthenticationFailureHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                .sessionManagement(session -> session
                        .invalidSessionUrl("/auth/login?timeout=true")
                        // <--- CAMBIO 1: Se cambió newSession() por changeSessionId() para que Spring Security no pierda el rastro de sesiones concurrentes
                        .sessionFixation(sf -> sf.changeSessionId())
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        // <--- CAMBIO 2: Se reemplazó .expiredUrl(...) por .expiredSessionStrategy(...) para soportar tanto navegación normal como peticiones HTMX
                        .expiredSessionStrategy(event -> {
                            HttpServletRequest request = event.getRequest();
                            HttpServletResponse response = event.getResponse();
                            String redirectUrl = request.getContextPath() + "/auth/login?newSession=true";

                            if ("true".equals(request.getHeader("HX-Request"))) {
                                response.setHeader("HX-Redirect", redirectUrl);
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            } else {
                                response.sendRedirect(redirectUrl);
                            }
                        })
                );

        return http.build();
    }


    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            String contextPath = request.getContextPath();

            if (exception instanceof CredentialsExpiredException) {
                response.sendRedirect(contextPath + "/auth/login?expired=true");
            } else if (exception instanceof LockedException) {
                response.sendRedirect(contextPath + "/auth/login?locked=true");
            } else if (exception instanceof DisabledException) {
                response.sendRedirect(contextPath + "/auth/login?disabled=true");
            } else {
                // Para BadCredentialsException o UsernameNotFoundException siempre damos un error genérico (estándar OWASP para evitar enumeración de usuarios)
                response.sendRedirect(contextPath + "/auth/login?error=true");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public org.springframework.security.web.session.HttpSessionEventPublisher httpSessionEventPublisher() {
        return new org.springframework.security.web.session.HttpSessionEventPublisher();
    }
}