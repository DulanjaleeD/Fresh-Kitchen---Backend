package com.dulanjali.kitchen.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.dulanjali.kitchen.dto.secured.SignUpDto;
import com.dulanjali.kitchen.dto.secured.UserDto;
import com.dulanjali.kitchen.entities.secure.User;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Mapper {
    private final ModelMapper modelMapper;

    public UserDto toUserDto(User userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }

    public User toUserEntity(SignUpDto signUpDto) {
        return modelMapper.map(signUpDto, User.class);
    }
}
