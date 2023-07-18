package com.example.gatosspringboot.controller.handlerException;

import com.example.gatosspringboot.dto.response.ExceptionDTO;
import com.example.gatosspringboot.dto.response.SimpleExceptionDTO;
import com.example.gatosspringboot.exception.ExistingException;
import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.exception.PersonNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice(annotations = RestController.class)
public class ApiControllerAdvice {

    private Logger logger= LoggerFactory.getLogger(ApiControllerAdvice.class);

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

    @ExceptionHandler(PersonNotFound.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public SimpleExceptionDTO notFound(PersonNotFound ex){
        return new SimpleExceptionDTO(HttpStatus.NOT_FOUND.value(),ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        //logger.info("excepcion: "+ex.getMessage());
        return new ExceptionDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
    }

}
