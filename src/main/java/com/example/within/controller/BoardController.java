package com.example.within.controller;

import com.example.within.security.UserDetailsImpl;
import com.example.within.dto.BoardRequestDto;
import com.example.within.dto.BoardResponseDto;
import com.example.within.entity.EmotionEnum;
import com.example.within.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/within")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping(value = "/boards", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createBoard(@RequestPart BoardRequestDto boardRequestDto,
                                         @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return boardService.create(boardRequestDto, userDetails.getUser(), imageFile);
    }

    @GetMapping("/boards")
    public ResponseEntity<Page<BoardResponseDto>> getBoards(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PageableDefault(size = 6, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable) {
        return boardService.getBoards(userDetails.getUser(),pageable);
    }

    @GetMapping("/boards/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return boardService.getBoard(boardId,userDetails.getUser());
    }

    @PutMapping(value = "/boards/{boardId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateBoard(@PathVariable Long boardId,
                                         @RequestPart BoardRequestDto boardRequestDto,
                                         @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails)throws IOException{
        return boardService.update(boardId, boardRequestDto, userDetails.getUser(), imageFile);
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
