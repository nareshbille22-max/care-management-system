package com.example.care_management_system.service;

import com.example.care_management_system.util.ClientAddress;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final String emailId;
    public EmailService(JavaMailSender mailSender,
                        TemplateEngine templateEngine,
                        @Value("${spring.mail.username}") String emailId) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.emailId = emailId;
    }

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Async
    public void sendEmail(String email, String name, String event, String url) throws MessagingException {
        switch (event) {
            case "register" -> sendRegistrationEmail(email, name, "http://");
            case "login" -> {
                LocalDateTime localDateTime = LocalDateTime.now();
                String ipAddress = ClientAddress.getClientIp();
                sendLoginAlertEmail(email, name, String.valueOf(localDateTime), ipAddress);
            }
            case "forgot-password" -> {
                sendForgotPasswordEmail(email, name, url);
                logger.info("Reset Url : {}", url);
            }
        }
    }

    public void sendHtmlMail(String to, String subject, String templateName, Map<String, Object> variables) throws MessagingException {
        try {
            Context context = new Context();
            context.setVariables(variables);

            String htmlContent = templateEngine.process(templateName, context);

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(new InternetAddress(emailId, "Care Buddy"));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Email sent successfully to {}", to);
        } catch (MessagingException e) {
            logger.error("Error sending email to {}", e.getMessage());
            throw e;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRegistrationEmail(String to, String name, String dashboardUrl) throws MessagingException {
        Map<String, Object> model = Map.of(
                "subject", "Registration Successful",
                "title", "Welcome to CareBuddy!",
                "name", name,
                "message", "Your registration was successful! We are excited to help you take care of your loved ones and pets while you focus on your work or travels.",
                "buttonText", "Go to Dashboard",
                "dashboardUrl", dashboardUrl
        );
        sendHtmlMail(to, "Registration Successful", "registration-success.html", model);
    }

    public void sendLoginAlertEmail(String to, String name, String loginTime, String ipAddress) throws MessagingException {
        Map<String, Object> model = Map.of(
                "subject", "Login Alert",
                "title", "New Login Detected",
                "name", name,
                "message", "We noticed a login to your CareBuddy account. If this was you, no action is needed. If you did not login, please reset your password immediately.",
                "loginTime", loginTime,
                "ipAddress", ipAddress
        );
        sendHtmlMail(to, "Login Alert", "login-alert.html", model);
    }

    public void sendForgotPasswordEmail(String to, String name, String resetUrl) throws MessagingException {
        Map<String, Object> model = Map.of(
                "subject", "Reset Your Password",
                "title", "Reset Your Password",
                "name", name,
                "message", "We received a request to reset your CareBuddy account password. Click the button below to reset it:",
                "buttonText", "Reset Password",
                "resetUrl", resetUrl
        );
        sendHtmlMail(to, "Reset Your Password", "forgot-password.html", model);
    }

}
