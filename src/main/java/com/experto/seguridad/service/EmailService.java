package com.experto.seguridad.service;


import com.experto.seguridad.dto.EmailRequest;
import com.experto.seguridad.model.EmailEvento;
import com.experto.seguridad.repository.EmailEventoRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.HtmlUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailEventoRepository emailEventoRepository;

    @Value("${spring.mail.username}")
    private String defaultRemitente;

    private static final String ESTADO_ACTIVO = "A";
    // Captura exactamente el patrón #{cualquier.cosa}
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("#\\{([^}]+)\\}");

    /**
     * Envía un correo de forma asíncrona utilizando una plantilla de base de datos.
     */
    @Async("taskExecutor") // Recomendado usar un Pool de hilos dedicado
    @Transactional(readOnly = true)
    public void enviarCorreoEvento(@Valid EmailRequest request) {
        log.info("Iniciando proceso de envío de correo para evento: [{}] hacia: [{}]",
                request.getCodigoEvento(), enmascararCorreo(request.getDestinatario()));

        EmailEvento evento = emailEventoRepository
                .findByCodigoAndEstado(request.getCodigoEvento(), ESTADO_ACTIVO)
                .orElseThrow(() -> {
                    log.error("Plantilla de correo no encontrada o inactiva para el código: {}", request.getCodigoEvento());
                    return new IllegalStateException("No existe configuración activa de correo para el evento: " + request.getCodigoEvento());
                });

        try {
            String cuerpoProcesado = procesarPlantilla(evento.getPlantilla(), request.getParametros());
            String asuntoProcesado = procesarTextoPlano(evento.getAsunto(), request.getParametros());

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            String remitente = StringUtils.hasText(evento.getEmailRemitente())
                    ? evento.getEmailRemitente().trim()
                    : defaultRemitente;

            helper.setFrom(remitente);
            helper.setTo(sanitizarCabecera(request.getDestinatario()));
            helper.setSubject(sanitizarCabecera(asuntoProcesado));
            helper.setText(cuerpoProcesado, true); // true = es HTML

            if (request.getCopias() != null && !request.getCopias().isEmpty()) {
                String[] ccArray = request.getCopias().stream()
                        .filter(StringUtils::hasText)
                        .map(this::sanitizarCabecera)
                        .toArray(String[]::new);
                helper.setCc(ccArray);
            }

            mailSender.send(mimeMessage);
            log.info("Correo enviado exitosamente. Evento: [{}], Destinatario: [{}]",
                    request.getCodigoEvento(), enmascararCorreo(request.getDestinatario()));

        } catch (MessagingException | MailException ex) {
            log.error("Error crítico enviando correo para evento [{}] a [{}]: {}",
                    request.getCodigoEvento(), enmascararCorreo(request.getDestinatario()), ex.getMessage(), ex);
            // Aquí podrías guardar en una tabla de auditoría/reintentos si tu negocio lo exige
        }
    }

    /**
     * Reemplaza los placeholders #{clave} escapando caracteres HTML para prevenir XSS/HTML Injection.
     */
    private String procesarPlantilla(String plantillaHtml, Map<String, String> parametros) {
        if (!StringUtils.hasText(plantillaHtml)) {
            return "";
        }
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(plantillaHtml);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String key = matcher.group(1); // Ej: "nombre.usuario"
            String rawValue = parametros.getOrDefault(key, "");
            // Escapamos el contenido dinámico para evitar inyección de HTML malicioso
            String safeValue = HtmlUtils.htmlEscape(rawValue != null ? rawValue : "");
            matcher.appendReplacement(sb, Matcher.quoteReplacement(safeValue));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * Reemplaza placeholders en el asunto (sin escape HTML porque es texto plano).
     */
    private String procesarTextoPlano(String texto, Map<String, String> parametros) {
        if (!StringUtils.hasText(texto)) {
            return "";
        }
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(texto);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = parametros.getOrDefault(key, "");
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value != null ? value : ""));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * Previene ataques SMTP Header Injection eliminando saltos de línea.
     */
    private String sanitizarCabecera(String valor) {
        return valor == null ? "" : valor.replaceAll("[\\r\\n]", "").trim();
    }

    /**
     * Enmascara el correo en los logs para cumplir con normativas de privacidad (LOPDP / GDPR).
     */
    private String enmascararCorreo(String email) {
        if (!StringUtils.hasText(email) || !email.contains("@")) {
            return "***";
        }
        String[] partes = email.split("@");
        String usuario = partes[0];
        String visible = usuario.length() > 2 ? usuario.substring(0, 2) : usuario.substring(0, 1);
        return visible + "***@" + partes[1];
    }
}

