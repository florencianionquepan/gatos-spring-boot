package com.example.gatosspringboot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonaEmailDTO {
    @NotNull
    @Email
    private String email;
}
