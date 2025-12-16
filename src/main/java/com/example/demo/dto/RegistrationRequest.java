package com.example.demo.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegistrationRequest {

    @NotBlank(message = "Username obbligatorio")
    @Size(min = 3, max = 20, message = "Username deve essere tra 3 e 20 caratteri")
    private String username;

    @NotBlank(message = "Password obbligatoria")
    @Size(min = 6, message = "Password deve essere almeno 6 caratteri")
    private String password;

    @NotBlank(message = "Email obbligatoria")
    @Email(message = "Email non valida")
    private String email;
}