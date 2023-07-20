package com.example.gatosspringboot.service.imple;

import com.example.gatosspringboot.service.interfaces.IEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
            SimpleMailMessage message=new SimpleMailMessage();
            message.setFrom("florencianionquepan@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            this.emailSender.send(message);
            return true;
        }catch (MailException ex){
            logger.error(ex.getMessage());
            return false;
        }
    }
}
