package com.example.within.repository;

import com.example.within.dto.BoardResponseDto;
import com.example.within.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("select new com.example.within.dto.BoardResponseDto(b) from Board b")
    Page<BoardResponseDto> selectAll(Pageable pageable);
}
