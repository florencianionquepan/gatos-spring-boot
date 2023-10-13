package com.example.gatosspringboot.service.interfaces;

public interface IEmailService {
    boolean sendMessage(String to, String subject, String text);
    boolean armarEnviarEmail(String to, String subject,String text);
}
