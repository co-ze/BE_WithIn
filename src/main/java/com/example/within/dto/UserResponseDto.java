package com.example.within.dto;

import com.example.within.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private String username;
    private String email;

    public UserResponseDto(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
