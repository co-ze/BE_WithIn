package com.example.within.dto;

import lombok.Getter;

@Getter
public class BoardRequestDto {
    private String title;
    private String contents;
    private String image;
    private String category;
}
