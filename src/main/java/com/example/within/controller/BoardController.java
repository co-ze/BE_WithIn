package com.example.within.controller;

import com.example.within.dto.BoardRequestDto;
import com.example.within.dto.BoardResponseDto;
import com.example.within.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/within")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/boards")
    public ResponseEntity<?> createBoard(@RequestBody BoardRequestDto boardRequestDto){
        return boardService.create(boardRequestDto);
    }

    @GetMapping("/boards")
    public ResponseEntity<List<BoardResponseDto>> getBoards() {
        return boardService.getBoards();
    }

    @GetMapping("/boards/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId){
        return boardService.getBoard(boardId);
    }

}
