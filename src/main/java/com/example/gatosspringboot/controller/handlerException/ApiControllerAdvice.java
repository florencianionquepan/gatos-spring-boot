package com.example.gatosspringboot.controller.handlerException;

import com.example.gatosspringboot.dto.response.ExceptionDTO;
import com.example.gatosspringboot.exception.ExistingException;
import com.example.gatosspringboot.exception.NonExistingException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice(annotations = RestController.class)
public class ApiControllerAdvice {

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO exceptionHandler(MethodArgumentNotValidException ex){
        List<FieldError> errorLIst= ex.getBindingResult().getFieldErrors();
        Map<String,String> detalle= new HashMap<>();
        errorLIst.forEach(e->detalle.put(e.getField(),e.getDefaultMessage()));
        return new ExceptionDTO(HttpStatus.BAD_REQUEST.value(),"Validaciones",detalle);
    }

    @ExceptionHandler(NonExistingException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO doesntExist(NonExistingException ex){
        return new ExceptionDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage(),null);
    }

    @ExceptionHandler(ExistingException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO doesntExist(ExistingException ex){
        return new ExceptionDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage(),null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handleIllegalArgumentException(IllegalArgumentException ex) {
        String errorMessage = "El estado proporcionado no es válido.";
        return new ExceptionDTO(HttpStatus.BAD_REQUEST.value(), errorMessage, null);
    }

}
