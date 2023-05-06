package com.example.within.controller;

import com.example.within.Security.UserDetailsImpl;
import com.example.within.dto.BoardRequestDto;
import com.example.within.dto.BoardResponseDto;
import com.example.within.entity.Board;
import com.example.within.entity.EmotionEnum;
import com.example.within.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/within")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/boards")
    public ResponseEntity<?> createBoard(@RequestBody BoardRequestDto boardRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails){
        return boardService.create(boardRequestDto, userDetails.getUser());
    }

    @GetMapping("/boards")
    public ResponseEntity<Page<Board>> getBoards(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PageableDefault(size = 3, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable) {
        return boardService.getBoards(userDetails.getUser(),pageable);
    }

    @GetMapping("/boards/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return boardService.getBoard(boardId,userDetails.getUser());
    }

    @PutMapping("/boards/{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable Long boardId,
                                         @RequestBody BoardRequestDto boardRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails){
        return boardService.update(boardId, boardRequestDto, userDetails.getUser());
    }

    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails){
        return boardService.delete(boardId, userDetails.getUser());
    }

    @PostMapping("/boards/{boardId}/{emotion}")
    public ResponseEntity<?> selectEmotion(@PathVariable Long boardId, @PathVariable EmotionEnum emotion, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return boardService.SelectEmotion(boardId, emotion, userDetails.getUser());
    }
}
