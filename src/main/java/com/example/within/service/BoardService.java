package com.example.within.service;

import com.example.within.dto.BasicResponseDto;
import com.example.within.dto.BoardRequestDto;
import com.example.within.dto.BoardResponseDto;
import com.example.within.entity.Board;
import com.example.within.entity.StatusCode;
import com.example.within.entity.User;
import com.example.within.entity.UserRoleEnum;
import com.example.within.exception.ErrorException;
import com.example.within.exception.ExceptionEnum;
import com.example.within.repository.BoardRepository;
import com.example.within.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> create(BoardRequestDto boardRequestDto, User user) {
        Board board = new Board(boardRequestDto);

        //ADMIN 확인
        isUserAdmin(user);

        // 유저 아이디 추가
        board.addUser(user);

        boardRepository.save(board);
        BasicResponseDto basicResponseDto =
                new BasicResponseDto(StatusCode.OK.getStatusCode(), "생성 성공!!");
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
                new BasicResponseDto(StatusCode.OK.getStatusCode(), "게시글 수정 성공!");
        return new ResponseEntity<>(basicResponseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> delete(Long boardId, User user) {
        // 게시글 존재여부 확인
        Board board = existBoard(boardId);

        // 작성자 게시글 체크
        isBoardUser(user, board);
        boardRepository.deleteById(boardId);
        BasicResponseDto basicResponseDto =
                new BasicResponseDto(StatusCode.OK.getStatusCode(), "게시글 삭제 성공!");
        return new ResponseEntity<>(basicResponseDto, HttpStatus.OK);
    }

    private Board existBoard(Long boardId){
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new ErrorException(ExceptionEnum.BOARD_NOT_FOUND)
        );
        return board;
    }

    private void isUserAdmin(User user){
        if(!user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new ErrorException(ExceptionEnum.NOT_AUTHORIZATION);
        }
    }

    private void isBoardUser(User user, Board board){
        if(!board.getUser().getEmail().equals(user.getEmail())){
            throw new ErrorException(ExceptionEnum.NOT_AUTHORIZATION);
        }
    }

    public User existUser(String email){
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ErrorException(ExceptionEnum.USER_NOT_FOUND)
        );
        return user;
    }

}
