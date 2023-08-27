package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.dto.validator.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldMatch(first = "password", second = "passwordConfirm", message = "Las contraseñas no coinciden")
//para registrarse
public class UsuarioReqDTO {
    @NotNull
    @Email
    private String email;
    @NotNull
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,}$", message = "La contraseña debe tener al menos 8 caracteres, una letra y un número.")
    private String password;
    @NotNull
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,}$", message = "La contraseña debe tener al menos 8 caracteres, una letra y un número.")
    private String passwordConfirm;
}
