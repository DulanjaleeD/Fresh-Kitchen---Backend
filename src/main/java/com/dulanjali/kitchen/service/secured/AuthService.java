package com.dulanjali.kitchen.service.secured;

import org.springframework.web.bind.annotation.RequestBody;

import com.dulanjali.kitchen.dto.secured.AuthResponseDto;
import com.dulanjali.kitchen.dto.secured.LoginDTO;
import com.dulanjali.kitchen.dto.secured.SignUpDto;


public interface AuthService {
    AuthResponseDto signUp(@RequestBody SignUpDto signUpDto);
    AuthResponseDto logIn(@RequestBody LoginDTO loginDTO);
}
