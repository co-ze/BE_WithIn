package com.example.within.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Emotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    @JsonIgnore
    private Board board;

    @ManyToOne
    @JoinColumn(name = "COMMENT_ID")
    @JsonIgnore
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EmotionEnum emotion;

    public Emotion(Board board, Comment comment, User user, EmotionEnum emotion) {
        this.board = board;
        this.comment = comment;
        this.user = user;
        this.emotion = emotion;
    }

    public void update(EmotionEnum emotion){
        this.emotion = emotion;
    }
}
