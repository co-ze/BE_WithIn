package com.example.within.service;

import com.example.within.dto.BasicResponseDto;
import com.example.within.dto.BoardRequestDto;
import com.example.within.dto.BoardResponseDto;
import com.example.within.entity.*;
import com.example.within.exception.ErrorException;
import com.example.within.exception.ExceptionEnum;
import com.example.within.repository.BoardRepository;
import com.example.within.repository.EmotionRepository;
import com.example.within.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final EmotionRepository emotionRepository;

    @Transactional
    public ResponseEntity<?> create(BoardRequestDto boardRequestDto, User user) {
        Board board = new Board(boardRequestDto);

        //ADMIN 확인
        isUserAdmin(user);

        // 유저 아이디 추가
        board.addUser(user);

        boardRepository.save(board);
        BasicResponseDto basicResponseDto =
                new BasicResponseDto(StatusCode.OK.getStatusCode(), "게시글을 작성하였습니다.");
        return new ResponseEntity<>(basicResponseDto, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<BoardResponseDto>> getBoards(User user) {
        existUser(user.getEmail());
        List<BoardResponseDto> boardList = boardRepository.findAll().stream()
                .sorted(Comparator.comparing(Board::getCreatedAt).reversed())
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(boardList, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getBoard(Long boardId, User user) {
        existUser(user.getEmail());
        // 게시글 존재여부 확인
        Board board = existBoard(boardId);
        BoardResponseDto boardResponseDto = new BoardResponseDto(board);
        return new ResponseEntity<>(boardResponseDto, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> update(Long boardId, BoardRequestDto boardRequestDto, User user) {
        // 게시글 존재여부 확인
        Board board = existBoard(boardId);

        // 작성자 게시글 체크
        isBoardUser(user, board);
        board.update(boardRequestDto);
        BasicResponseDto basicResponseDto =
                new BasicResponseDto(StatusCode.OK.getStatusCode(), "게시글을 수정하였습니다.");
        return new ResponseEntity<>(basicResponseDto, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> delete(Long boardId, User user) {
        // 게시글 존재여부 확인
        Board board = existBoard(boardId);

        // 작성자 게시글 체크
        isBoardUser(user, board);
        boardRepository.deleteById(boardId);
        BasicResponseDto basicResponseDto =
                new BasicResponseDto(StatusCode.OK.getStatusCode(), "게시글을 삭제하였습니다.");
        return new ResponseEntity<>(basicResponseDto, HttpStatus.OK);
    }

    private Board existBoard(Long boardId){
        return boardRepository.findById(boardId).orElseThrow(
                () -> new ErrorException(ExceptionEnum.BOARD_NOT_FOUND)
        );
    }

    private void isUserAdmin(User user){
        if(!user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new ErrorException(ExceptionEnum.NOT_ALLOWED);
        }
    }

    private void isBoardUser(User user, Board board){
        if(!board.getUser().getEmail().equals(user.getEmail())){
            throw new ErrorException(ExceptionEnum.NOT_ALLOWED);
        }
    }

    public void existUser(String email){
        userRepository.findByEmail(email).orElseThrow(
                () -> new ErrorException(ExceptionEnum.USER_NOT_FOUND)
        );
    }

    @Transactional
    public ResponseEntity<?> SelectEmotion(Long boardId, EmotionEnum emotion, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new NoSuchElementException("게시글이 존재하지 않습니다.")
        );
        Emotion toEmotion = new Emotion(board, null, user, emotion);
        Emotion existingEmotion = emotionRepository.findByBoardIdAndUserIdAndEmotion(boardId, user.getId(), emotion);

        BasicResponseDto basicResponseDto;
        String message;
        long emotionCnt;

        if (existingEmotion != null) {
            emotionRepository.delete(existingEmotion);
            message = getEmotionString(emotion) + " 취소";
        } else {
            emotionRepository.save(toEmotion);
            message = getEmotionString(emotion) + " 등록";
        }
        emotionCnt = emotionRepository.findAllBoardCntEachEmotion(boardId, emotion);

        switch (emotion) {
            case LIKE -> board.setLikeCnt(emotionCnt);
            case SAD -> board.setSadCnt(emotionCnt);
            case CONGRATULATION -> board.setCongratulationCnt(emotionCnt);
            default -> throw new IllegalStateException("Unexpected value: " + emotion);
        }

        boardRepository.save(board);

        basicResponseDto = new BasicResponseDto(StatusCode.OK.getStatusCode(), message);
        return new ResponseEntity<>(basicResponseDto, HttpStatus.OK);
    }

    private String getEmotionString(EmotionEnum emotion) {
        return switch (emotion) {
            case LIKE -> "좋아요";
            case SAD -> "슬퍼요";
            case CONGRATULATION -> "추카해요";
        };
    }
}
