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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return new ResponseEntity<>(BasicResponseDto.addSuccess(StatusCode.OK.getStatusCode(), "게시글을 작성하였습니다."), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Page<BoardResponseDto>> getBoards(User user, Pageable pageable) {
        existUser(user.getEmail());
        Page<BoardResponseDto> boardList = boardRepository.selectAll(pageable);
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
        return new ResponseEntity<>(BasicResponseDto.addSuccess(StatusCode.OK.getStatusCode(), "게시글을 수정하였습니다."), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> delete(Long boardId, User user) {
        // 게시글 존재여부 확인
        Board board = existBoard(boardId);

        // 작성자 게시글 체크
        isBoardUser(user, board);
        boardRepository.deleteById(boardId);
        return new ResponseEntity<>(BasicResponseDto.addSuccess(StatusCode.OK.getStatusCode(), "게시글을 삭제하였습니다."), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> SelectEmotion(Long boardId, EmotionEnum emotion, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new ErrorException(ExceptionEnum.BOARD_NOT_FOUND)
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
        return new ResponseEntity<>(BasicResponseDto.addSuccess(StatusCode.OK.getStatusCode(), message), HttpStatus.OK);
    }

    private String getEmotionString(EmotionEnum emotion) {
        return switch (emotion) {
            case LIKE -> "좋아요";
            case SAD -> "슬퍼요";
            case CONGRATULATION -> "추카해요";
        };
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
}
