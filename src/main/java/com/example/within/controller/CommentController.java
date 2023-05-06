package com.example.within.controller;

import com.example.within.Security.UserDetailsImpl;
import com.example.within.dto.CommentRequestDto;
import com.example.within.entity.EmotionEnum;
import com.example.within.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/within/boards")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{boardId}/comments")
    public ResponseEntity<?> createComment(@PathVariable Long boardId,
                                           @RequestBody CommentRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.createComment(boardId,requestDto,userDetails.getUser());
    }

    @PutMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long boardId, @PathVariable Long commentId,
                                           @RequestBody CommentRequestDto commentRequestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(boardId,commentId,commentRequestDto,userDetails.getUser());
    }


    @DeleteMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long boardId,
                                           @PathVariable Long commentId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.deleteComment(boardId,commentId,userDetails.getUser());
    }

    @PostMapping("/{boardId}/comments/{commentId}/{emotion}")
    public ResponseEntity<?> selectEmotion(@PathVariable Long boardId, @PathVariable Long commentId, @PathVariable EmotionEnum emotion, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.SelectEmotion(boardId, commentId, emotion, userDetails.getUser());
    }

}
