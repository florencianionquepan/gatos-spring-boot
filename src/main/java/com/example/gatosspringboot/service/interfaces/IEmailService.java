package com.example.gatosspringboot.service.interfaces;

public interface IEmailService {
    void sendMessage(String to, String subject, String text);
}
