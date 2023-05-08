package com.example.within.dto;

import com.example.within.entity.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "add")
public class BasicResponseDto {
    private int statusCode;
    private String message;

    public static BasicResponseDto addSuccess(int statusCode, String message){
        return BasicResponseDto.add(statusCode, message);
    }
    public static BasicResponseDto addBadRequest(String message){
        return BasicResponseDto.add(StatusCode.BAD_REQUEST.getStatusCode(), message);
    }
}
