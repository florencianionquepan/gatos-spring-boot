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
    private Logger logger= LoggerFactory.getLogger(EmailService.class);

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public boolean sendMessage(String to, String subject, String text) {
        try{
            //SimpleMailMessage message=new SimpleMailMessage();
            MimeMessage message= emailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(message,true, "UTF-8");
            helper.setFrom("florencianionquepan@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text,true);
            this.emailSender.send(message);
            return true;
        }catch (MailException ex){
            logger.error(ex.getMessage());
            return false;
        } catch (MessagingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
