package com.example.within.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BasicResponseDto {
    private int statusCode;
    private String message;
}
