package com.example.within.dto;

import lombok.Getter;

@Getter
public class UserResponseDto {
    private String username;
    private String email;

    public UserResponseDto(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
