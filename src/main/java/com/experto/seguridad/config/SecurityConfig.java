package com.experto.seguridad.config;

import com.experto.seguridad.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            UsuarioService usuarioService,
            RecaptchaFilter recaptchaFilter
    ) throws Exception {
        http
                // CAMBIO 1 (OWASP): Eliminamos .csrf(csrf -> csrf.disable()) para activar la protección nativa.

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/css/**", "/js/**", "/assets/**", "/vite.svg").permitAll()
                        .requestMatchers("/dashboard/**").authenticated()
                        .anyRequest().authenticated()
                )
                // Ejecutar el validador del reCAPTCHA antes del filtro de login nativo
                .addFilterBefore(recaptchaFilter, UsernamePasswordAuthenticationFilter.class)

                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .successHandler(customAuthenticationSuccessHandler(usuarioService))
                        // CAMBIO 2: Registramos el manejador de fallos que lee la caducidad
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
                        .sessionFixation(sf -> sf.newSession())
                        .maximumSessions(1)
                );

        return http.build();
    }

    // CAMBIO 3: Creamos el Bean que atrapa el error de expiración y redirige a la vista
    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            if (exception instanceof CredentialsExpiredException) {
                response.sendRedirect("/auth/login?expired=true");
            } else {
                response.sendRedirect("/auth/login?error=true");
            }
        };
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler(UsuarioService usuarioService) {
        return (request, response, authentication) -> {
            HttpSession session = request.getSession();
            var dto = usuarioService.obtenerDatosSesion(authentication.getName());
            session.setAttribute("USER_SESSION", dto);
            response.sendRedirect("/dashboard");
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
}