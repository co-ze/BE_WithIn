package com.example.within.repository;

import com.example.within.entity.Emotion;
import com.example.within.entity.EmotionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {

    @Query("select count (e) from Emotion e where e.board.id = :boardId and e.emotion = :emotion")
    Long findAllBoardCntEachEmotion(@Param("boardId") Long boardId, @Param("emotion")EmotionEnum emotion);

    @Query("select count (e) from Emotion e where e.comment.id = :commentId and e.emotion = :emotion")
    Long findAllCommentCntEachEmotion(@Param("commentId") Long commentId, @Param("emotion")EmotionEnum emotion);

    @Query("select case when (count(e) > 0) then true else false end from Emotion e where e.board.id = :boardId and e.emotion = :emotion and e.user.id = :userId")
    boolean findBoardEmotionWhoCheck(@Param("boardId") Long boardId, @Param("emotion")EmotionEnum emotion, @Param("userId") Long userId);

    @Query("select case when (count(e) > 0) then true else false end from Emotion e where e.comment.id = :commentId and e.emotion = :emotion and e.user.id = :userId")
    boolean findCommentEmotionWhoCheck(@Param("commentId") Long commentId, @Param("emotion")EmotionEnum emotion, @Param("userId") Long userId);

    Emotion findByBoardIdAndUserIdAndEmotion(Long boardId, Long userId, EmotionEnum emotion);

    Emotion findByCommentIdAndUserIdAndEmotion(Long commentId, Long userId, EmotionEnum emotion);
}
