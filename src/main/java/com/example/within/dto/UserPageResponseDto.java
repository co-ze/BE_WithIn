package com.example.within.dto;

import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPageResponseDto {
    private String username;
    private String email;
    private String img;

    public UserPageResponseDto(String username, String email, String img) {
        this.username = username;
        this.email = email;
        this.img = img;
    }
}
