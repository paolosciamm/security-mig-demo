package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
  @GetMapping("/")
  public String home() {
    return "Hello secured world";
  }

  @GetMapping("/public/ping")
  public String ping() {
    return "pong";
  }
}
