package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.service.interfaces.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

    final JavaMailSender emailSender;
    private Logger logger = LoggerFactory.getLogger(EmailService.class);

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public boolean sendMessage(String to, String subject, String text) {
        try {
            //SimpleMailMessage message=new SimpleMailMessage();
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("florencianionquepan@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            this.emailSender.send(message);
            return true;
        } catch (MailException ex) {
            logger.error(ex.getMessage());
            return false;
        } catch (MessagingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean armarEnviarEmail(String to, String subject, String text) {
        String url = "http://localhost:4200/login/";
        String content = "<div style=\"background-color: #F5CDFF; border-radius: 10px; padding: 10px; font-size: 20px;\">"
                + "<p>"+text+"</p>"
                + "<p style=\"margin-bottom: 5px;\">Inicia sesión para obtener más detalles:</p>"
                + "<a href=\"" + url + "\" style=\"display: inline-block; color: #202124; padding: 10px 20px; text-decoration: none; font-weight: bold; font-size: 16px; background-color: transparent; border: 2px solid #202124; border-radius: 10px;\">Iniciar sesión</a>"
                + "</div>";
        return this.sendMessage(to, subject, content);
    }
}
