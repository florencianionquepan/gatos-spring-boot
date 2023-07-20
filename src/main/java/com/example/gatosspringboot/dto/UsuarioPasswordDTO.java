package com.example.gatosspringboot.dto;

import com.example.gatosspringboot.dto.validator.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(first = "passwordNew", second = "passwordNewConfirm", message = "Las contraseñas no coinciden")
public class UsuarioPasswordDTO {
    @NotNull
    @Email
    private String email;
    @NotNull
    private String passwordActual;
    @NotNull
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,}$", message = "La contraseña debe tener al menos 8 caracteres, una letra y un número.")
    private String passwordNew;
    @NotNull
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,}$", message = "La contraseña debe tener al menos 8 caracteres, una letra y un número.")
    private String passwordNewConfirm;
}
