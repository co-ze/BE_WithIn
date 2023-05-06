package com.example.within.repository;

import com.example.within.entity.Emotion;
import com.example.within.entity.EmotionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {

    @Query("select count (e) from Emotion e where e.board.id = :boardId and e.emotion = :emotion")
    Long findAllCntEachEmotion(@Param("boardId") Long boardId, @Param("emotion")EmotionEnum emotion);

    Emotion findByBoardIdAndUserIdAndEmotion(Long boardId, Long userId, EmotionEnum emotion);
}
