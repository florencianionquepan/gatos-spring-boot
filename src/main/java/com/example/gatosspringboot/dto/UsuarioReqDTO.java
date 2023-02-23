package com.example.gatosspringboot.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioReqDTO {
    private Long id;
    private String mail;
    private String password;
}
