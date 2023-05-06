package com.example.within.dto;

import com.example.within.entity.User;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
