package com.example.gatosspringboot.exception;

public class NonExistingException extends RuntimeException{
    public NonExistingException(String message){
        super(message);
    }
}
