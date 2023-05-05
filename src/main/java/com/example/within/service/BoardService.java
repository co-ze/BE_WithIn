package com.example.within.service;

import com.example.within.dto.BasicResponseDto;
import com.example.within.dto.BoardRequestDto;
import com.example.within.entity.Board;
import com.example.within.entity.StatusCode;
import com.example.within.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public ResponseEntity<?> create(BoardRequestDto boardRequestDto) {
        Board board = new Board(boardRequestDto);

        // 유저 아이디 추가

        boardRepository.save(board);
        BasicResponseDto basicResponseDto = new BasicResponseDto(StatusCode.OK.getStatusCode(), "생성 성공!!");
        return new ResponseEntity<>(basicResponseDto, HttpStatus.OK);
    }
}
