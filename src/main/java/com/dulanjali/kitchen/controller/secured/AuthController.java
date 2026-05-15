package com.dulanjali.kitchen.controller.secured;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dulanjali.kitchen.dto.secured.AuthResponseDto;
import com.dulanjali.kitchen.dto.secured.LoginDTO;
import com.dulanjali.kitchen.dto.secured.SignUpDto;
import com.dulanjali.kitchen.service.secured.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<String> get() {
        System.out.println("hit end point");
        return ResponseEntity.ok("Online");
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signup(@RequestBody SignUpDto signUpDto) {
        return ResponseEntity.ok(authService.signUp(signUpDto));
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDTO login) {
        return ResponseEntity.ok(authService.logIn(login));
    }
}