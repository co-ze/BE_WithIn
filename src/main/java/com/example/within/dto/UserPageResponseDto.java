package com.example.within.dto;

import com.example.within.entity.UserRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserPageResponseDto {
    private String username;
    private UserRoleEnum role;
    private String email;
    private String img;

    public UserPageResponseDto(String username, UserRoleEnum role, String email, String img) {
        this.username = username;
        this.role = role;
        this.email = email;
        this.img = img;
    }
}
