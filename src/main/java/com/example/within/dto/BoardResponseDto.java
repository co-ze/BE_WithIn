package com.example.within.dto;

import com.example.within.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private String title;
    private String contents;
    private LocalDateTime createdTime;
    private String category;

    public BoardResponseDto(Board board){
        this.title = board.getTitle();
        this.contents =  board.getContents();
        this.createdTime = board.getCreatedAt();
        this.category = board.getCategory();
    }
}
