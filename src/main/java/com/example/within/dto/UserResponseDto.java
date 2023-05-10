package com.example.within.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private Long userId;
    private String username;
    private String email;
    private String img;

    public UserResponseDto(Long userId, String username, String email, String img) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.img = img;
    }
}
