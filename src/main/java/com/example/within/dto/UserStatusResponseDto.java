package com.example.within.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserStatusResponseDto {
    private String username;
    private String message;
}
