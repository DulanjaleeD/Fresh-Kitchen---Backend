package com.dulanjali.kitchen.dto.secured;

import java.io.Serializable;

import com.dulanjali.kitchen.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto implements Serializable {
    private String id;
    private String email;
    private String role;

    public static UserDto of(String id, String email, Role role) {
        return UserDto.builder()
                .id(id)
                .email(email)
                .role(role.name())
                .build();
    }
}
