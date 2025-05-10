package com.eventure.events.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendHtmlEmail(String to, String subject, String templatePath, Map<String, String> variables) throws Exception {
        try {
            logger.info("Attempting to send email to: {}", to);
            
            String htmlContent = loadTemplate(templatePath, variables);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            // Attach logo image
            try {
                ClassPathResource logo = new ClassPathResource("EventureLogo.jpeg");
                helper.addInline("logo", logo);
                logger.info("Logo attachment successful");
            } catch (Exception e) {
                logger.error("Failed to attach logo: {}", e.getMessage());
                // Continue without logo if it fails
            }

            mailSender.send(message);
            logger.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage(), e);
            throw e;
        }
    }

    private String loadTemplate(String templatePath, Map<String, String> variables) throws Exception {
        try {
            ClassPathResource resource = new ClassPathResource(templatePath);
            String content = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                content = content.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
            return content;
        } catch (Exception e) {
            logger.error("Failed to load email template {}: {}", templatePath, e.getMessage());
            throw e;
        }
    }
} 