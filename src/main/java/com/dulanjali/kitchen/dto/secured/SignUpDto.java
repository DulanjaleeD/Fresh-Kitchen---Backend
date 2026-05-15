package com.dulanjali.kitchen.dto.secured;

import java.io.Serializable;

import com.dulanjali.kitchen.entities.Cart;
import com.dulanjali.kitchen.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpDto implements Serializable{
    private String id;
    private String email;
    private String password;
    private Role role;
    private Cart cart;
}
