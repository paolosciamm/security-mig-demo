package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    @Value("${app.mail.from:noreply@example.com}")
    private String defaultFrom;

    public void sendMorningGreetings(List<String> emails) {
        for (String email : emails) {
            try {
                sendEmail(email, "Test mail", "Bla bla bla");
                log.info("Email inviata con successo a: {}", email);
            } catch (Exception e) {
                // log già presente: mantieni, ma MailException è più specifica
                log.error("Errore durante l'invio dell'email a: {}", email, e);
            }
        }
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(defaultFrom);
        try {
            mailSender.send(message);
        } catch (MailException me) {
            // log e rilanciare o swallow in base al comportamento voluto
            log.error("Problema SMTP per destinatario {}: {}", to, me.getMessage());
            throw me; // oppure gestisci localmente senza rilanciare
        }
    }
}

