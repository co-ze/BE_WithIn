package com.example.within.dto;

import com.example.within.entity.Board;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoardDetailResponseDto {
    private Long boardId;
    private String title;
    private String contents;
    private Long userId;
    private String image;
    private LocalDateTime createdTime;
    private String category;
    private Long likeCnt;
    private Long sadCnt;
    private Long congratulationCnt;
    private boolean likeCheck = false;
    private boolean sadCheck = false;
    private boolean congratulationCheck = false;
    private List<CommentDetailResponseDto> commentDetailResponseDtoList;

    public BoardDetailResponseDto(Board board, List<CommentDetailResponseDto> commentDetailResponseDtoList){
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.contents =  board.getContents();
        this.userId = board.getUser().getId();
        this.image = board.getImage();
        this.createdTime = board.getCreatedAt();
        this.category = board.getCategory();
        this.likeCnt = board.getLikeCnt();
        this.sadCnt = board.getSadCnt();
        this.congratulationCnt = board.getCongratulationCnt();
        this.commentDetailResponseDtoList = commentDetailResponseDtoList;
    }
}
