package com.example.demo.dto;

import lombok.Data;

@Data
class LoginResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
}
