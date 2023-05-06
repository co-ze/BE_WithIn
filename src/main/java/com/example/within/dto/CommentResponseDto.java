package com.example.within.dto;

import com.example.within.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private String comment;
    private String username;
    private LocalDateTime createdAt;
    private Long likeCnt;
    private Long sadCnt;
    private Long congratulationCnt;


    public CommentResponseDto(Comment comment){
        this.comment = comment.getComment();
        this.username = comment.getUser().getUsername();
        this.createdAt = comment.getCreatedAt();
        this.likeCnt = comment.getLikeCnt();
        this.sadCnt = comment.getSadCnt();
        this.congratulationCnt = comment.getCongratulationCnt();
    }
}
