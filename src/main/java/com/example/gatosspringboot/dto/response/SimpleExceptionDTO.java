package com.example.gatosspringboot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleExceptionDTO {
    private int estado;
    private String mensaje;
}
