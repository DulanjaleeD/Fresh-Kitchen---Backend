package com.dulanjali.kitchen.service.secured.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import com.dulanjali.kitchen.dao.UserDao;
import com.dulanjali.kitchen.dto.secured.AuthResponseDto;
import com.dulanjali.kitchen.dto.secured.LoginDTO;
import com.dulanjali.kitchen.dto.secured.SignUpDto;
import com.dulanjali.kitchen.entities.secure.User;
import com.dulanjali.kitchen.securityConfig.JwtUtils;
import com.dulanjali.kitchen.service.secured.AuthService;
import com.dulanjali.kitchen.util.IDGenerate;
import com.dulanjali.kitchen.util.Mapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final Mapper mapper;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDto signUp(SignUpDto signUpDto) {
        if (userDao.existsByEmail(signUpDto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        signUpDto.setId(IDGenerate.userId());
        signUpDto.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        var savedUser = userDao.save(mapper.toUserEntity(signUpDto));
        var token = jwtUtils.generateToken(savedUser.getEmail(), savedUser.getAuthorities());

        return AuthResponseDto.builder()
                .token(token)
                .build();
    }

    @Override
    public AuthResponseDto logIn(LoginDTO loginDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        User user = userDao.findByEmail(loginDTO.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        var token = jwtUtils.generateToken(user.getUsername(), user.getAuthorities());

        return AuthResponseDto.builder()
                .token(token)
                .build();
    }

}
