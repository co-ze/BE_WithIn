package com.example.within.service;

import com.example.within.dto.BasicResponseDto;
import com.example.within.dto.CommentRequestDto;
import com.example.within.entity.*;
import com.example.within.repository.BoardRepository;
import com.example.within.repository.CommentRepository;
import com.example.within.repository.EmotionRepository;
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
    private final EmotionRepository emotionRepository;
    private final UserRepository userRepository;


    @Transactional
    public ResponseEntity<?> createComment(Long boardId, CommentRequestDto requestDto, User user) {
        Board board = existBoard(boardId);
        Comment comment = new Comment(requestDto);

        comment.addBoard(board);
        comment.addUser(user);

        commentRepository.save(comment);
        BasicResponseDto basicResponseDto =
                new BasicResponseDto(StatusCode.OK.getStatusCode(), "댓글을 작성하였습니다.");
        return new ResponseEntity<>(basicResponseDto, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateComment(Long boardId, Long commentId, CommentRequestDto commentRequestDto, User user) {
        Board board = existBoard(boardId);
        Comment comment = existComment(commentId);

        isCommentUser(user, comment);

        comment.update(commentRequestDto);
        BasicResponseDto basicResponseDto =
                new BasicResponseDto(StatusCode.OK.getStatusCode(), "댓글을 수정하였습니다.");
        return new ResponseEntity<>(basicResponseDto, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteComment(Long boardId, Long commentId, User user) {
        Board board = existBoard(boardId);
        Comment comment = existComment(commentId);

        isCommentUser(user,comment);

        commentRepository.deleteById(commentId);
        BasicResponseDto basicResponseDto =
                new BasicResponseDto(StatusCode.OK.getStatusCode(), "댓글을 삭제하였습니다.");
        return new ResponseEntity<>(basicResponseDto, HttpStatus.OK);
    }

    private Board existBoard(Long boardId){
        return boardRepository.findById(boardId).orElseThrow(
                () -> new NoSuchElementException("게시글이 존재하지 않습니다.")
        );
    }

    private void isCommentUser(User user, Comment comment){
        if(!comment.getUser().getEmail().equals(user.getEmail())){
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    private Comment existComment(Long id){
        return commentRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("댓글이 존재하지 않습니다.")
        );
    }

    @Transactional
    public ResponseEntity<?> SelectEmotion(Long boardId, Long commentId, EmotionEnum emotion, User user) {
        Board board = existBoard(boardId);
        Comment comment = existComment(commentId);
        Emotion toEmotion = new Emotion(null, comment, user, emotion);
        Emotion existingEmotion = emotionRepository.findByCommentIdAndUserIdAndEmotion(commentId, user.getId(), emotion);

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
        emotionCnt = emotionRepository.findAllCommentCntEachEmotion(commentId, emotion);

        switch (emotion) {
            case LIKE -> comment.setLikeCnt(emotionCnt);
            case SAD -> comment.setSadCnt(emotionCnt);
            case CONGRATULATION -> comment.setCongratulationCnt(emotionCnt);
            default -> throw new IllegalStateException("Unexpected value: " + emotion);
        }

        commentRepository.save(comment);

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
