package com.example.within.service;

import com.example.within.dto.BasicResponseDto;
import com.example.within.dto.BoardRequestDto;
import com.example.within.dto.BoardResponseDto;
import com.example.within.entity.Board;
import com.example.within.entity.StatusCode;
import com.example.within.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public ResponseEntity<?> create(BoardRequestDto boardRequestDto) {
        Board board = new Board(boardRequestDto);

        // 유저 아이디 추가

        boardRepository.save(board);
        BasicResponseDto basicResponseDto =
                new BasicResponseDto(StatusCode.OK.getStatusCode(), "생성 성공!!");
        return new ResponseEntity<>(basicResponseDto, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<BoardResponseDto>> getBoards() {
        List<BoardResponseDto> boardList = boardRepository.findAll().stream()
                .sorted(Comparator.comparing(Board::getCreatedAt).reversed())
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(boardList, HttpStatus.OK);

    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getBoard(Long boardId) {
        // 게시글 존재여부 확인
        Board board = existBoard(boardId);
        BoardResponseDto boardResponseDto = new BoardResponseDto(board);
        return new ResponseEntity<>(boardResponseDto, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> update(Long boardId, BoardRequestDto boardRequestDto) {
        // 게시글 존재여부 확인
        Board board = existBoard(boardId);

        // 작성자 게시글 체크

        board.update(boardRequestDto);
        BasicResponseDto basicResponseDto =
                new BasicResponseDto(StatusCode.OK.getStatusCode(), "게시글 수정 성공!");
        return new ResponseEntity<>(basicResponseDto, HttpStatus.OK);
    }

    private Board existBoard(Long boardId){
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new NoSuchElementException("게시글이 존재하지 않습니다.")
        );
        return board;
    }

}
