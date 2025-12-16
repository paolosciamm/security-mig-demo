package com.example.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {

    @NotBlank(message = "Username obbligatorio")
    private String username;

    @NotBlank(message = "Password obbligatoria")
    private String password;
}