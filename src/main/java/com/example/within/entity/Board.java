package com.example.within.entity;

import com.example.within.dto.BoardRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Board extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String category;

    @Lob
    private String image;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public Board(BoardRequestDto boardRequestDto){
        this.title = boardRequestDto.getTitle();
        this.contents = boardRequestDto.getContents();
        this.category = boardRequestDto.getCategory();
        this.image = boardRequestDto.getImage();
    }

    public void update(BoardRequestDto boardRequestDto){
        this.title = boardRequestDto.getTitle();
        this.contents = boardRequestDto.getContents();
        this.category = boardRequestDto.getCategory();
        this.image = boardRequestDto.getImage();
    }

    public void addUser(User user){
        this.user = user;
    }

}
