package com.chubb.api_gateway.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {


    @GetMapping("/health")
    public ResponseEntity<String> health(){
        return ResponseEntity.ok("OK");
    }
}
