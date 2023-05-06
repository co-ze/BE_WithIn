package com.example.within.entity;

import com.example.within.dto.CommentRequestDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    @JsonBackReference
    private Board board;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public Comment(CommentRequestDto commentRequestDto){
        this.comment = commentRequestDto.getComment();
    }

    public void addUser(User user){
        this.user = user;
    }

    public void addBoard(Board board){
        this.board = board;
    }

}
