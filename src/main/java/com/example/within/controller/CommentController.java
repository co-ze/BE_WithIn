package com.example.within.controller;

import com.example.within.Security.UserDetailsImpl;
import com.example.within.dto.CommentRequestDto;
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

}
