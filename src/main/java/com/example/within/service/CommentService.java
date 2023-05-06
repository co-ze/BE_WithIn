package com.example.within.service;

import com.example.within.dto.BasicResponseDto;
import com.example.within.dto.CommentRequestDto;
import com.example.within.entity.Board;
import com.example.within.entity.Comment;
import com.example.within.entity.StatusCode;
import com.example.within.entity.User;
import com.example.within.repository.BoardRepository;
import com.example.within.repository.CommentRepository;
import com.example.within.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> createComment(Long boardId, CommentRequestDto requestDto, User user) {
        Board board = existBoard(boardId);
        Comment comment = new Comment(requestDto);

        comment.addBoard(board);
        comment.addUser(user);

        commentRepository.save(comment);
        BasicResponseDto basicResponseDto =
                new BasicResponseDto(StatusCode.OK.getStatusCode(), "댓글 작성 성공!");
        return new ResponseEntity<>(basicResponseDto, HttpStatus.OK);
    }

    private Board existBoard(Long boardId){
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new NoSuchElementException("게시글이 존재하지 않습니다.")
        );
        return board;
    }

    private void isBoardUser(User user, Board board){
        if(!board.getUser().getEmail().equals(user.getEmail())){
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    private void isCommentUser(User user, Comment comment){
        if(!comment.getUser().getEmail().equals(user.getEmail())){
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    public User existUser(String email){
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NoSuchElementException("사용자가 없습니다.")
        );
        return user;
    }


}
