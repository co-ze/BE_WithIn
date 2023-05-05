package com.example.within.entity;

import com.example.within.dto.BoardRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String image;

//    @ManyToOne
//    private User user;

    public Board(BoardRequestDto boardRequestDto){
        this.title = boardRequestDto.getTitle();
        this.contents = boardRequestDto.getContents();
        this.category = boardRequestDto.getCategory();
        this.image = boardRequestDto.getImage();

    }

}
