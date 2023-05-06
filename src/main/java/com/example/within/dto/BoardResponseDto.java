package com.example.within.dto;

import com.example.within.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<CommentResponseDto> comment;

    public BoardResponseDto(Board board){
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.contents =  board.getContents();
        this.createdTime = board.getCreatedAt();
        this.category = board.getCategory();
        this.likeCnt = board.getLikeCnt();
        this.sadCnt = board.getSadCnt();
        this.congratulationCnt = board.getCongratulationCnt();
        this.comment = board.getCommentList().stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }
}
