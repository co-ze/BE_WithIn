package com.example.within.dto;

import com.example.within.entity.Board;
import jakarta.persistence.Column;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private final Long boardId;
    private final String title;
    private final String contents;
    private final LocalDateTime createdTime;
    private final String category;
    private final Long likeCnt;
    private final Long sadCnt;
    private final Long congratulationCnt;

    public BoardResponseDto(Board board){
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.contents =  board.getContents();
        this.createdTime = board.getCreatedAt();
        this.category = board.getCategory();
        this.likeCnt = board.getLikeCnt();
        this.sadCnt = board.getSadCnt();
        this.congratulationCnt = board.getCongratulationCnt();
    }
}
